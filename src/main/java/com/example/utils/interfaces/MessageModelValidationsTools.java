package com.example.utils.interfaces;

import java.io.File;
import java.util.List;

public interface MessageModelValidationsTools {
    boolean validFiles(List<File> files);

    boolean validMessageData(String sender, String acceptor, String subject, String message, List<String> kopies);
}
