DELETE FROM tech_talker_bd.t_user_roles
WHERE user_id IN (SELECT id FROM tech_talker_bdt_user WHERE username = 'Иван1')
AND roles_id IN (SELECT id FROM tech_talker_bd.t_roles WHERE name_role = 'ROLE_USER');