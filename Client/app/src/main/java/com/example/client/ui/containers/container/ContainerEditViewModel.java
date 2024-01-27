package com.example.client.ui.containers.container;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.client.data.model.Container;

public class ContainerEditViewModel extends ViewModel {
    private long containerId;
    private Container container;
    private MutableLiveData<Container> containerMutableLiveData;

    private ContainerEditViewModel() {
        setContainer();
    }

    private void setContainer() {
        getFromDB();

        containerMutableLiveData = new MutableLiveData<>();
        containerMutableLiveData.setValue(container);
    }

    private void getFromDB() {
        //todo get from db or saved db on device

        getContainerInfo();
    }

    private void getContainerInfo() {
        container = new Container(
                5L,
                "Mock Container"
        );
    }

    public void setContainerId(long containerId) {
        this.containerId = containerId;
    }

    public MutableLiveData<Container> getContainerMutableLiveData() {
        return containerMutableLiveData;
    }

    public void updateContainer(Container editedValues) {
        //todo look the difference between old and new container and see differences and send them to be modified
    }

    public void deleteContainer() {
        //todo delete container with id container id
    }
}