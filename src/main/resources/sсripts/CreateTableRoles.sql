CREATE TABLE IF NOT EXISTS tech_talker_bd.t_roles (
                                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                      name_role VARCHAR(255) NOT NULL
);

INSERT INTO tech_talker_bd.t_roles (id, name_role) VALUES (1, 'ROLE_USER'), (2, 'ROLE_ADMIN')
ON DUPLICATE KEY UPDATE name_role = VALUES(name_role);
