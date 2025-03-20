CREATE TABLE IF NOT EXISTS PRODUCTS (
                                        ID INT PRIMARY KEY,
                                        TYPE VARCHAR(50),
                                        NAME VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS TRANSACTIONS (
                                            ID INT PRIMARY KEY,
                                            PRODUCT_ID INT,
                                            USER_ID INT,
                                            AMOUNT DECIMAL,
                                            FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCTS(ID)
);