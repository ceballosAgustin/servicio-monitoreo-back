INSERT INTO plantas (id_planta, nombre, pais, bandera, lecturas, alertas_medias, alertas_rojas, sensores_deshabilitados)
SELECT 1, 'Quilmes', 'Argentina', 'https://flagcdn.com/ar.svg', 300, 10, 2, 0
WHERE NOT EXISTS (
    SELECT * FROM plantas WHERE id_planta = 1 AND nombre = 'Quilmes' AND pais = 'Argentina'
);

INSERT INTO plantas (id_planta, nombre, pais, bandera, lecturas, alertas_medias, alertas_rojas, sensores_deshabilitados)
SELECT 2, 'Zarate', 'Argentina', 'https://flagcdn.com/ar.svg', 100, 15, 2, 0
WHERE NOT EXISTS (
    SELECT * FROM plantas WHERE id_planta = 2 AND nombre = 'Zarate' AND pais = 'Argentina'
);

INSERT INTO plantas (id_planta, nombre, pais, bandera, lecturas, alertas_medias, alertas_rojas, sensores_deshabilitados)
SELECT 3, 'Sao Pablo', 'Brasil', 'https://flagcdn.com/br.svg', 400, 40, 2, 0
WHERE NOT EXISTS (
    SELECT * FROM plantas WHERE id_planta = 3 AND nombre = 'Sao Pablo' AND pais = 'Brasil'
);

INSERT INTO plantas (id_planta, nombre, pais, bandera, lecturas, alertas_medias, alertas_rojas, sensores_deshabilitados)
SELECT 4, 'Asuncion', 'Paraguay', 'https://flagcdn.com/py.svg', 321, 9, 2, 0
WHERE NOT EXISTS (
    SELECT * FROM plantas WHERE id_planta = 4 AND nombre = 'Asuncion' AND pais = 'Paraguay'
);

INSERT INTO plantas (id_planta, nombre, pais, bandera, lecturas, alertas_medias, alertas_rojas, sensores_deshabilitados)
SELECT 5, 'Montevideo', 'Uruguay', 'https://flagcdn.com/uy.svg', 434, 4, 4, 0
WHERE NOT EXISTS (
    SELECT * FROM plantas WHERE id_planta = 5 AND nombre = 'Montevideo' AND pais = 'Uruguay'
);