package fr.redboxing.wakfu.server.utils.serialization;

public interface BinarSerialDataSource
{
    void updateToSerializedPart();

    void onDataChanged();
}