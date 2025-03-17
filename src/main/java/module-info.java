module org.example.fosdesktop {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires spring.web;
    requires static lombok;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires spring.context;
    requires jdk.httpserver;
    requires spring.beans;
    requires org.apache.tomcat.embed.core;
    requires com.fasterxml.jackson.databind;
    requires spring.core;
    requires java.desktop;
    requires java.prefs;

    opens org.example.fosdesktop to javafx.fxml, spring.core, spring.context;
    opens org.example.fosdesktop.controller to javafx.fxml, spring.core, spring.beans, spring.context;
    opens org.example.fosdesktop.config to javafx.fxml, spring.core, spring.beans, spring.context;

    exports org.example.fosdesktop;
    exports org.example.fosdesktop.model.dto;
    exports org.example.fosdesktop.config;
    exports org.example.fosdesktop.controller;
    exports org.example.fosdesktop.service;
}
