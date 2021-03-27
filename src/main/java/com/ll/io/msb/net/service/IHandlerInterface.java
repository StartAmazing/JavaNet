package com.ll.io.msb.net.service;

import java.io.IOException;

@FunctionalInterface
public interface IHandlerInterface {

    void Handler(Request request, Response response) throws IOException;
}
