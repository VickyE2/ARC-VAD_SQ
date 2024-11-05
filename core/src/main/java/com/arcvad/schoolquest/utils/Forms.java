package com.arcvad.schoolquest.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import static com.arcvad.schoolquest.global.GlobalServer.socket;

import java.util.HashMap;
import java.util.Map;

public class Forms {
    private Skin skin;
    private Stage stage;

    public Forms(Skin skin, Stage stage){
        this.skin = skin;
        this.stage = stage;
    }

    public void openRegisterForm() {
        Table table = new Table();
        table.setFillParent(true);
        table.pad(10);

        TextField firstnameField = new TextField("", skin);
        TextField lastnameField = new TextField("", skin);
        TextField usernameField = new TextField("", skin);
        TextField emailField = new TextField("", skin);
        TextField passwordField = new TextField("", skin);
        TextField confirmPasswordField = new TextField("", skin);

        TextButton switchButton = new TextButton("Login", skin);

        Label firstNameError = new Label("", skin);
        Label lastNameError = new Label("", skin);
        Label usernameError = new Label("", skin);
        Label emailError = new Label("", skin);
        Label passwordError = new Label("", skin);
        Label confirmPasswordError = new Label("", skin);

        passwordField.setPasswordMode(true);
        confirmPasswordField.setPasswordMode(true);

        TextButton submitButton = new TextButton("Register", skin);

        // Top row with firstname and lastname
        table.add(new Label("First Name", skin)).left();
        table.add(new Label("Last Name", skin)).right().padLeft(10);
        table.row();
        table.add(firstnameField).width(Value.percentWidth(0.5f, table)).padBottom(10).align(Align.topLeft);
        table.add(lastnameField).width(Value.percentWidth(0.5f, table)).padBottom(10).align(Align.topRight);
        table.row();

        // Middle rows with username, email, password, confirm password
        table.add(new Label("Username", skin)).colspan(2).center().padBottom(5);
        table.row();
        table.add(usernameField).colspan(2).width(Value.percentWidth(1f, table)).padBottom(10);
        table.row();

        table.add(new Label("Email", skin)).colspan(2).center().padBottom(5);
        table.row();
        table.add(emailField).colspan(2).width(Value.percentWidth(1f, table)).padBottom(10);
        table.row();

        table.add(new Label("Password", skin)).colspan(2).center().padBottom(5);
        table.row();
        table.add(passwordField).colspan(2).width(Value.percentWidth(1f, table)).padBottom(10);
        table.row();

        table.add(new Label("Confirm Password", skin)).colspan(2).center().padBottom(5);
        table.row();
        table.add(confirmPasswordField).colspan(2).width(Value.percentWidth(1f, table)).padBottom(10);
        table.row();

        firstNameError.setColor(Color.RED);
        lastNameError.setColor(Color.RED);
        usernameError.setColor(Color.RED);
        emailError.setColor(Color.RED);
        passwordError.setColor(Color.RED);
        confirmPasswordError.setColor(Color.RED);

        table.add(firstnameField).width(Value.percentWidth(0.5f, table)).top().pad(10);
        table.row();
        table.add(firstNameError).width(Value.percentWidth(0.5f, table)).padBottom(10);

        table.add(lastnameField).width(Value.percentWidth(0.5f, table)).top().pad(10);
        table.row();
        table.add(lastNameError).width(Value.percentWidth(0.5f, table)).padBottom(10);

        table.add(usernameField).width(Value.percentWidth(1.0f, table)).top().pad(10);
        table.row();
        table.add(usernameError).width(Value.percentWidth(1.0f, table)).padBottom(10);

        table.add(emailField).width(Value.percentWidth(1.0f, table)).top().pad(10);
        table.row();
        table.add(emailError).width(Value.percentWidth(1.0f, table)).padBottom(10);

        table.add(passwordField).width(Value.percentWidth(1.0f, table)).top().pad(10);
        table.row();
        table.add(passwordError).width(Value.percentWidth(1.0f, table)).padBottom(10);

        table.add(confirmPasswordField).width(Value.percentWidth(1.0f, table)).top().pad(10);
        table.row();
        table.add(confirmPasswordError).width(Value.percentWidth(1.0f, table)).padBottom(10);


        // Bottom row with submit button
        table.add(submitButton).colspan(2).width(Value.percentWidth(1f, table)).padTop(10);
        table.row();
        table.add(switchButton).colspan(2).left().padTop(10);

        // Add the table to the stage
        stage.addActor(table);

        switchButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                table.clear(); // Clear current form
                openLoginForm(); // Open login form
            }
        });

        submitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isValid = true;

                // Clear previous error messages
                firstNameError.setText("");
                lastNameError.setText("");
                usernameError.setText("");
                emailError.setText("");
                passwordError.setText("");
                confirmPasswordError.setText("");

                // Validation checks
                if (firstnameField.getText().isEmpty()) {
                    firstNameError.setText("First name is required.");
                    isValid = false;
                }
                if (lastnameField.getText().isEmpty()) {
                    lastNameError.setText("Last name is required.");
                    isValid = false;
                }
                if (usernameField.getText().isEmpty()) {
                    usernameError.setText("Username is required.");
                    isValid = false;
                }
                if (!emailField.getText().contains("@")) {
                    emailError.setText("Invalid email address.");
                    isValid = false;
                }
                if (passwordField.getText().isEmpty()) {
                    passwordError.setText("Password is required.");
                    isValid = false;
                }
                if (!confirmPasswordField.getText().equals(passwordField.getText())) {
                    confirmPasswordError.setText("Passwords do not match.");
                    isValid = false;
                }

                if (isValid) {
                    Map<String, String> registeredData = new HashMap<>();
                    registeredData.put("firstName", firstnameField.getName());
                    registeredData.put("lastName", lastnameField.getName());
                    registeredData.put("username", usernameField.getName());
                    registeredData.put("email", emailField.getName());
                    registeredData.put("password", passwordField.getName());

                    Json json = new Json();
                    String jsonValue = json.toJson(registeredData, HashMap.class);

                    socket.send("registerUser->" + jsonValue);

                }
            }
        });

        firstnameField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                firstNameError.setText("");
            }
        });

        lastnameField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                lastNameError.setText("");
            }
        });

        usernameField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                usernameError.setText("");
            }
        });

        passwordField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                passwordError.setText("");
            }
        });

        confirmPasswordField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                confirmPasswordError.setText("");
            }
        });

        emailField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                emailError.setText("");
            }
        });
    }

    public void openLoginForm() {
        Table table = new Table();
        table.setFillParent(true);
        table.pad(10);

        TextField usernameField = new TextField("", skin);
        TextField passwordField = new TextField("", skin);

        Label usernameError = new Label("", skin);
        Label passwordError = new Label("", skin);

        passwordField.setPasswordMode(true);

        TextButton loginButton = new TextButton("Login", skin);
        TextButton switchButton = new TextButton("Register", skin);

        table.add(new Label("Username", skin)).colspan(2).center().padBottom(5);
        table.row();
        table.add(usernameField).colspan(2).width(Value.percentWidth(1f, table)).padBottom(10);
        table.row();

        table.add(new Label("Password", skin)).colspan(2).center().padBottom(5);
        table.row();
        table.add(passwordField).colspan(2).width(Value.percentWidth(1f, table)).padBottom(10);
        table.row();

        table.add(new Label("Username", skin)).colspan(2).center().padBottom(5);
        table.row();
        table.add(usernameError).colspan(2).width(Value.percentWidth(1f, table)).padBottom(10);
        table.row();

        table.add(new Label("Password", skin)).colspan(2).center().padBottom(5);
        table.row();
        table.add(passwordField).colspan(2).width(Value.percentWidth(1f, table)).padBottom(10);
        table.row();

        usernameError.setColor(Color.RED);
        passwordError.setColor(Color.RED);

        table.add(loginButton).colspan(2).width(Value.percentWidth(1f, table)).padTop(10);
        table.row();
        table.add(switchButton).colspan(2).left().padTop(10);

        // Add the table to the stage
        stage.addActor(table);

        switchButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                table.clear(); // Clear current form
                openRegisterForm(); // Open registration form
            }
        });

        loginButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                boolean isValid = true;

                usernameError.setText("");
                passwordError.setText("");

                if (!usernameField.getText().contains("@")) {
                    usernameError.setText("Invalid email address.");
                    isValid = false;
                }
                if (passwordField.getText().isEmpty()) {
                    passwordError.setText("Password is required.");
                    isValid = false;
                }

                if (isValid) {
                    Map<String, String> registeredData = new HashMap<>();
                    registeredData.put("email", usernameField.getName());
                    registeredData.put("password", passwordField.getName());

                    Json json = new Json();
                    String jsonValue = json.toJson(registeredData, HashMap.class);

                    socket.send("requestUser->" + jsonValue);

                }
            }
        });

        passwordField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                passwordError.setText("");
            }
        });


        usernameField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                usernameError.setText("");
            }
        });
    }

    public void closeForm(Table table) {
        table.remove();
    }

}
