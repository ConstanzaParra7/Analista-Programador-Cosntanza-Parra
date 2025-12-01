
--CASO1
SELECT
  p.id_profesional                                    AS "ID",
  INITCAP(p.appaterno || ' ' || p.apmaterno
          || ' ' || p.nombre)                         AS "PROFESIONAL",
  b.num_banca                                         AS "NRO ASESORIA BANCA",
  '$' || TO_CHAR(b.total_banca, 'FM999G999G999')      AS "MONTO_TOTAL_BANCA",
  r.num_retail                                        AS "NRO ASESORIA RETAIL",
  '$' || TO_CHAR(r.total_retail, 'FM999G999G999')     AS "MONTO_TOTAL_RETAIL",
  (b.num_banca + r.num_retail)                        AS "TOTAL ASESORIAS",
  '$' || TO_CHAR(b.total_banca + r.total_retail,
                 'FM999G999G999')                     AS "TOTAL HONORARIOS"

FROM (
        SELECT a.id_profesional
        FROM   asesoria a
        JOIN   empresa e ON e.cod_empresa = a.cod_empresa
        WHERE  e.cod_sector = 3
        GROUP BY a.id_profesional

        INTERSECT

        --reatil
        SELECT a.id_profesional
        FROM   asesoria a
        JOIN   empresa e ON e.cod_empresa = a.cod_empresa
        WHERE  e.cod_sector = 4
        GROUP BY a.id_profesional
     ) ambos

JOIN (
        SELECT a.id_profesional,
               COUNT(*) AS num_banca,
               SUM(a.honorario) AS total_banca
        FROM   asesoria a
        JOIN   empresa e ON e.cod_empresa = a.cod_empresa
        WHERE  e.cod_sector = 3
        GROUP BY a.id_profesional
     ) b
  ON b.id_profesional = ambos.id_profesional

JOIN (
        SELECT a.id_profesional,
               COUNT(*) AS num_retail,
               SUM(a.honorario) AS total_retail
        FROM   asesoria a
        JOIN   empresa e ON e.cod_empresa = a.cod_empresa
        WHERE  e.cod_sector = 4
        GROUP BY a.id_profesional
     ) r
  ON r.id_profesional = ambos.id_profesional

JOIN profesional p
  ON p.id_profesional = ambos.id_profesional

ORDER BY p.id_profesional ASC;

--CASO 2--

CREATE TABLE REPORTE_MES AS
SELECT
    p.id_profesional                                            AS "ID",
    INITCAP(p.appaterno || ' ' || p.apmaterno || ' ' || p.nombre)
                                                                AS "NOMBRE_COMPLETO",
    pr.nombre_profesion                                         AS "NOMBRE_PROFESION",
    NVL(c.nom_comuna, 'SIN COMUNA')                             AS "NOM_COMUNA",
    COUNT(*)                                                    AS "NRO_ASESORIAS",
    ROUND(SUM(a.honorario))                                     AS "MONTO_TOTAL_HONORARIOS",
    ROUND(AVG(a.honorario))                                     AS "PROMEDIO_HONORARIO",
    ROUND(MIN(a.honorario))                                     AS "HONORARIO_MINIMO",
    ROUND(MAX(a.honorario))                                     AS "HONORARIO_MAXIMO"
FROM   profesional p
       JOIN asesoria a   ON a.id_profesional = p.id_profesional
       JOIN profesion pr ON pr.cod_profesion = p.cod_profesion
       JOIN comuna c     ON c.cod_comuna    = p.cod_comuna
WHERE  EXTRACT(MONTH FROM a.fin_asesoria) = 4
AND    EXTRACT(YEAR  FROM a.fin_asesoria) =
       EXTRACT(YEAR FROM ADD_MONTHS(SYSDATE, -12))
GROUP BY
       p.id_profesional,
       p.appaterno,
       p.apmaterno,
       p.nombre,
       pr.nombre_profesion,
       c.nom_comuna
ORDER BY
       p.id_profesional;

---CASO 3
-- 1) REPORTE ANTES DE MODIFICAR SUELDOS:profesionales con asesorías terminadas y en MARZO del año pasado

SELECT
    p.id_profesional                                            AS "ID",
    INITCAP(p.appaterno || ' ' || p.apmaterno || ' ' || p.nombre)
                                                                AS "PROFESIONAL",
    ROUND(SUM(a.honorario))                                     AS "TOTAL_HONOR_MARZO",
    p.sueldo                                                    AS "SUELDO_ACTUAL"
FROM   profesional p
       JOIN asesoria a
           ON a.id_profesional = p.id_profesional
WHERE  EXTRACT(MONTH FROM a.fin_asesoria) = 3
AND    EXTRACT(YEAR  FROM a.fin_asesoria) =
       EXTRACT(YEAR FROM ADD_MONTHS(SYSDATE, -12))
GROUP BY
       p.id_profesional,
       p.appaterno,
       p.apmaterno,
       p.nombre,
       p.sueldo
ORDER BY
       p.id_profesional;

------------------------------------------------
-- 2) ACTUALIZACIÓN DE SUELDOS
--    < 1.000.000 total honorarios marzo  -> +10%
--    >= 1.000.000 total honorarios marzo -> +15%
------------------------------------------------

-- Opcional en caso de querer volver atrás
SAVEPOINT antes_update_sueldo;

UPDATE profesional p
SET    p.sueldo =
       ROUND(
         p.sueldo *
         CASE
             WHEN (SELECT SUM(a.honorario)
                   FROM   asesoria a
                   WHERE  a.id_profesional = p.id_profesional
                   AND    EXTRACT(MONTH FROM a.fin_asesoria) = 3
                   AND    EXTRACT(YEAR  FROM a.fin_asesoria) =
                          EXTRACT(YEAR FROM ADD_MONTHS(SYSDATE, -12))
                  ) < 1000000
             THEN 1.10          -- aumento 10%
             ELSE 1.15          -- aumento 15%
         END
       )
WHERE  p.id_profesional IN (
       SELECT a.id_profesional
       FROM   asesoria a
       WHERE  EXTRACT(MONTH FROM a.fin_asesoria) = 3
       AND    EXTRACT(YEAR  FROM a.fin_asesoria) =
              EXTRACT(YEAR FROM ADD_MONTHS(SYSDATE, -12))
       GROUP BY a.id_profesional
);

-- Ingresar en caso de que los datos estén ok
COMMIT;

-- en caso de deshacer las pruebas antes del COMMIT:
-- ROLLBACK TO antes_update_sueldo;


-- 3) REPORTE DESPUÉS DE MODIFICAR SUELDOS

SELECT
    p.id_profesional                                            AS "ID",
    INITCAP(p.appaterno || ' ' || p.apmaterno || ' ' || p.nombre)
                                                                AS "PROFESIONAL",
    ROUND(SUM(a.honorario))                                     AS "TOTAL_HONOR_MARZO",
    p.sueldo                                                    AS "SUELDO_ACTUAL"
FROM   profesional p
       JOIN asesoria a
           ON a.id_profesional = p.id_profesional
WHERE  EXTRACT(MONTH FROM a.fin_asesoria) = 3
AND    EXTRACT(YEAR  FROM a.fin_asesoria) =
       EXTRACT(YEAR FROM ADD_MONTHS(SYSDATE, -12))
GROUP BY
       p.id_profesional,
       p.appaterno,
       p.apmaterno,
       p.nombre,
       p.sueldo
ORDER BY
       p.id_profesional;

