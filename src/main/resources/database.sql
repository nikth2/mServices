DROP PROCEDURE IF EXISTS get_segments_by_activity;
/
CREATE PROCEDURE get_segments_by_activity(IN input_id INT )
BEGIN
    
    SELECT * FROM segment WHERE activity_id=input_id;
    
END;
/
commit;
/