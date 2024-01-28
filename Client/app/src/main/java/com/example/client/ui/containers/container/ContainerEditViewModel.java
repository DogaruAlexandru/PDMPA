package com.example.client.ui.containers.container;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.client.data.api.ContainerAPI;
import com.example.client.data.model.Container;

import java.io.IOException;

public class ContainerEditViewModel extends ViewModel {
    private long containerId;
    private Container container;
    private MutableLiveData<Container> containerMutableLiveData;

    private ContainerEditViewModel() {
    }

    public void setData() {
        getFromDB();

        containerMutableLiveData = new MutableLiveData<>();
        containerMutableLiveData.setValue(container);
    }

    private void getFromDB() {
        getContainerInfo();
    }

    private void getContainerInfo() {
        try {
            container = ContainerAPI.getContainer(containerId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        container = new Container(
//                5L,
//                "Mock Container"
//        );
    }

    public void setContainerId(long containerId) {
        this.containerId = containerId;
    }

    public MutableLiveData<Container> getContainerMutableLiveData() {
        return containerMutableLiveData;
    }

    public void updateContainer(Container editedValues) {
        try {
            ContainerAPI.updateContainer(editedValues);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteContainer() {
        try {
            ContainerAPI.deleteContainer(containerId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}