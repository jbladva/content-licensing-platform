package com.clp;

public interface Views {

    interface PublicView{}

    interface WriterView extends PublicView{}

    interface PublisherView extends PublicView{}
}
