## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

## To run the program, you will need to download the javafx resources
Enter vs code and go to src and click the "App.java" code and run it, an error like "Error: JavaFX runtime components are missing, and are required to run this application", you can fix it easily, go to "EXPLORER", click "vscode", under vscode click the "launch.json" and at the end of the last line put a come and enter a new line and write the following line, "vmArgs": "--module-path "path/to/javafx-sdk-22.0.2/lib" --add-modules javafx.controls,javafx.fxml", replace "path/to/javafx-sdk-22.0.2/lib" with the path to the javafx you have downloaded. Then it should run.
