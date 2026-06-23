ALTER TABLE Medicos ADD foto_perfil VARCHAR(255);
GO

UPDATE Medicos SET foto_perfil = 'doc_m_1.jpg' WHERE id_medico = 1;
UPDATE Medicos SET foto_perfil = 'doc_f_1.jpg' WHERE id_medico = 2;
UPDATE Medicos SET foto_perfil = 'doc_m_2.jpg' WHERE id_medico = 3;
UPDATE Medicos SET foto_perfil = 'doc_f_2.jpg' WHERE id_medico = 4;
UPDATE Medicos SET foto_perfil = 'doc_m_3.jpg' WHERE id_medico = 5;
UPDATE Medicos SET foto_perfil = 'doc_f_3.jpg' WHERE id_medico = 6;
UPDATE Medicos SET foto_perfil = 'doc_m_4.jpg' WHERE id_medico = 7;
UPDATE Medicos SET foto_perfil = 'doc_f_4.jpg' WHERE id_medico = 8;
UPDATE Medicos SET foto_perfil = 'doc_f_5.jpg' WHERE id_medico = 9;
UPDATE Medicos SET foto_perfil = 'doc_m_5.jpg' WHERE id_medico = 10;
UPDATE Medicos SET foto_perfil = 'doc_f_6.jpg' WHERE id_medico = 11;
UPDATE Medicos SET foto_perfil = 'doc_m_6.jpg' WHERE id_medico = 12;
UPDATE Medicos SET foto_perfil = 'doc_f_7.jpg' WHERE id_medico = 13;
UPDATE Medicos SET foto_perfil = 'doc_m_7.jpg' WHERE id_medico = 14;
UPDATE Medicos SET foto_perfil = 'doc_f_8.jpg' WHERE id_medico = 15;
UPDATE Medicos SET foto_perfil = 'doc_m_8.jpg' WHERE id_medico = 16;
UPDATE Medicos SET foto_perfil = 'doc_f_9.jpg' WHERE id_medico = 17;
UPDATE Medicos SET foto_perfil = 'doc_f_10.jpg' WHERE id_medico = 18;
UPDATE Medicos SET foto_perfil = 'doc_m_9.jpg' WHERE id_medico = 19;
UPDATE Medicos SET foto_perfil = 'doc_f_11.jpg' WHERE id_medico = 20;
UPDATE Medicos SET foto_perfil = 'doc_m_10.jpg' WHERE id_medico = 21;
UPDATE Medicos SET foto_perfil = 'doc_f_1.jpg' WHERE id_medico = 22; -- Reusing doc_f_1
UPDATE Medicos SET foto_perfil = 'doc_m_11.jpg' WHERE id_medico = 23;
UPDATE Medicos SET foto_perfil = 'doc_f_2.jpg' WHERE id_medico = 24; -- Reusing doc_f_2

GO
