package fr.redboxing.wakfu.server;

import fr.redboxing.wakfu.server.network.ServerInitializer;
import fr.redboxing.wakfu.server.utils.MySQLHelper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WakfuServer {
    private Logger logger = LogManager.getLogger("WakfuServer");
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private static WakfuServer instance;

    private MySQLHelper mySQLHelper;

    public WakfuServer() {
        instance = this;
        this.mySQLHelper = new MySQLHelper("localhost", "wakfu", "root", "root");
        this.mySQLHelper.open();
    }

    public void startAuthServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).localAddress(5558).childHandler(new ServerInitializer());

                try {
                    logger.info("Auth Server Listening on port 5558.");
                    logger.info("Auth Server took " + (System.currentTimeMillis() - start) + " milliseconds to start up.");
                    b.bind().awaitUninterruptibly().channel().closeFuture().awaitUninterruptibly();
                } catch (Exception e) {
                    logger.info("Auth Server Could not listen to port 5558.", e);
                }
            }
        }).start();
    }

    public void stopAuthServer() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        logger.info("Server Stopped.");
    }

    public void startGameServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long start = System.currentTimeMillis();
                ServerBootstrap b = new ServerBootstrap();
                b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).localAddress(5556).childHandler(new ServerInitializer());

                try {
                    logger.info("Game Server Listening on port 5556.");
                    logger.info("Game Server took " + (System.currentTimeMillis() - start) + " milliseconds to start up.");
                    b.bind().awaitUninterruptibly().channel().closeFuture().awaitUninterruptibly();
                } catch (Exception e) {
                    logger.info("Game Server Could not listen to port 5556.", e);
                }
            }
        }).start();
    }

    public void stopGameServer() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        logger.info("Server Stopped.");
    }

    public static void main(String[] args) {
        List<String> argsList = Arrays.asList(args);

        WakfuServer server = new WakfuServer();

        if((!argsList.contains("--authServer") && !argsList.contains("--gameServer")) || args.length == 0) {
            server.getLogger().info("No arguments specified, considering auth server.");
            server.startAuthServer();
        }else if(argsList.contains("--authServer") && argsList.contains("--gameServer")) {
            server.startAuthServer();
            server.startGameServer();
        }else if(args[0].equals("--authServer")) {
            server.startAuthServer();
        }else if(args[0].equals("--gameServer")) {
            server.startGameServer();
        }

        if(argsList.contains("--startGame")) {
            launchWakfu();
        }
    }

    public static void launchWakfu() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                WakfuServer.getInstance().getLogger().info("Launching Wakfu.");

                try {
                    String home = System.getProperty("user.home");
                    String wakfuDir = home + "/AppData/Local/Ankama/zaap/wakfu";
                    String cpu = "x64";

                    String xms = "512m";
                    String xmx = "2048m";

                    String[] options = new String[]{
                            "-XX:+UnlockExperimentalVMOptions",
                            "-XX:+UseG1GC",
                            "-XX:G1NewSizePercent=20",
                            "-XX:G1ReservePercent=20",
                            "-Djava.net.preferIPv4Stack=true",
                            "-Dsun.awt.noerasebackground=true",
                            "-Dsun.java2d.noddraw=true",
                            "-Djogl.disable.openglarbcontext",
                    };

                    ArrayList<String> args = new ArrayList<>();
                    args.add(String.format("%s/jre/win32/%s/bin/java.exe", wakfuDir, cpu));

                    args.add("-Xms" + xms);
                    args.add("-Xmx" + xmx);

                    args.addAll(Arrays.asList(options));

                    args.add("-Djava.library.path=natives/win32/" + cpu);

                    args.add("-cp");
                    args.add(wakfuDir + "/lib/*");

                    args.add("com.ankamagames.wakfu.client.WakfuClient");

                    ProcessBuilder builder = new ProcessBuilder();
                    builder.directory(new File(wakfuDir));
                    builder.command(args);
                    Map<String, String> env = builder.environment();
                    env.put("WAKFU_LANGUAGE", "fr");
                    env.put("WAKFU_CONFIG_FILE_PATH", new File("config.properties").getAbsolutePath());
                    env.put("WAKFU_PREF_FILE_DIRECTORY", new File("").getAbsolutePath());
                    env.put("WAKFU_CACHE_FILE_DIRECTORY", new File("cache/").getAbsolutePath());

                    Process p = builder.start();

                    try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                        String line;

                        while ((line = input.readLine()) != null) {
                            System.out.println(line);
                        }
                    }
                }catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    public MySQLHelper getMySQLHelper() {
        return mySQLHelper;
    }

    public Logger getLogger() {
        return logger;
    }

    public static WakfuServer getInstance() {
        return instance;
    }
}
