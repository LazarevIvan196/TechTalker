INSERT INTO tech_talker_bd.t_user_roles (user_id, roles_id)
SELECT u.id, r.id
FROM tech_talker_bd.t_user u, tech_talker_bd.t_roles r
WHERE u.username = 'Иван666' AND r.name_role= 'ROLE_ADMIN'
ON DUPLICATE KEY UPDATE roles_id = roles_id;