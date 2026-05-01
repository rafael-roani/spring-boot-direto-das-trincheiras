package dev.rafa.userservice.commons;

import java.io.File;
import java.nio.file.Files;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class FileUtils {

  @Autowired
  private ResourceLoader resourceLoader;

  @SneakyThrows
  public String readResourceFile(String fileName) {
    File file = resourceLoader.getResource("classpath:%s".formatted(fileName)).getFile();
    return new String(Files.readAllBytes(file.toPath()));
  }

}
