-- Create the database
CREATE DATABASE college_complaints;

-- Use the database
USE college_complaints;


CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE admin (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default admin login credentials
INSERT INTO admin (username, password) 
VALUES ('admin', 'admin123');


CREATE TABLE complaints (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    description TEXT NOT NULL,
    status ENUM('Pending', 'In Progress', 'Resolved') DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);


INSERT INTO users (name, email, password)
VALUES 
('Arjun P', 'arjun@gmail.com', '12345'),
('Karthik R', 'karthik@gmail.com', 'abcd123'),
('Divya P', 'divya@gmail.com', 'divya321');


INSERT INTO complaints (user_id, title, description, status)
VALUES
(1, 'Hostel Water Issue', 'Water not available in bathrooms after 9 PM.', 'Pending'),
(2, 'WiFi Not Working', 'College WiFi is disconnected in labs frequently.', 'In Progress'),
(3, 'Canteen Food', 'Quality of food in canteen is poor and needs improvement.', 'Resolved');
