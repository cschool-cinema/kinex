CREATE OR REPLACE FUNCTION
    is_screening_conflict(pscreening_start timestamp WITH TIME ZONE,
                          pscreening_end timestamp WITH TIME ZONE,
                          pauditorium_id int)
    RETURNS boolean AS
$$
BEGIN
    RETURN (
        CASE
            WHEN (SELECT COUNT(s.id)
                  FROM screening s
                           JOIN movie mv ON s.movie_id = mv.id
                  WHERE s.auditorium_id = pauditorium_id
                    AND s.screening_start < pscreening_end
                    AND s.screening_start + (INTERVAL '1 minute' * mv.duration_min) + INTERVAL '15 minutes' >
                        pscreening_start) > 0
                THEN TRUE
            ELSE FALSE
            END);
END;
$$
    LANGUAGE PLPGSQL;

---------------------------------------------------

CREATE OR REPLACE FUNCTION
    get_screening_end(pscreening_id int) RETURNS timestamp WITH TIME ZONE AS
$$
BEGIN
    RETURN (
        SELECT (s.screening_start + (INTERVAL '1 minute' * mv.duration_min) + INTERVAL '15 minutes') screening_end
        FROM screening s
                 JOIN movie mv ON s.movie_id = mv.id
        WHERE s.id = pscreening_id);
END;
$$
    LANGUAGE PLPGSQL;