-- password = "password"

INSERT INTO users (id, email, password, role)
SELECT '11111111-1111-1111-1111-111111111111', 'admin@example.com', '$2a$10$5dtU.DMwLp2CbqkYOa.Te.HKISrNZGbs5x8zczh3ec3VSYO3h0FY.', 'ADMIN'
    WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE id = '11111111-1111-1111-1111-111111111111'
);
