package com.example.client.ui.home;

public class ExpirationTodayModel {
    private final String produceName, containerName;

    public ExpirationTodayModel(String name, String container) {
        this.produceName = name;
        this.containerName = container;
    }

    public String getProduceName() {
        return produceName;
    }

    public String getContainerName() {
        return containerName;
    }

}
