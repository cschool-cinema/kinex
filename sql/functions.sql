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