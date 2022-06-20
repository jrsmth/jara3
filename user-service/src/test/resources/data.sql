-- used to initialise H2 for testing --
INSERT INTO J3_USER (id, username, password, active) VALUES (UUID(), 'usernameDuplicate', 'passwordDuplicate', true);