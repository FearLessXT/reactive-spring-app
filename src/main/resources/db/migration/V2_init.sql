CREATE TABLE customer (
                          customer_id SERIAL NOT NULL PRIMARY KEY,
                          wallet_number VARCHAR(255),
                          first_name VARCHAR(255),
                          last_name VARCHAR(255),
                          gender VARCHAR(10),
                          dob VARCHAR(20),
                          nationality VARCHAR(20),
                          id_type VARCHAR(20),
                          id_number VARCHAR(20),
                          id_expiration_date VARCHAR(20),
                          account_type VARCHAR(10),
                          photo_of_id  VARCHAR(255),
                          selfie_photo VARCHAR(255),
                          pin VARCHAR(10)
)