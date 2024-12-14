CREATE TABLE address (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  city VARCHAR(250) NOT NULL,
  UF VARCHAR(250) NOT NULL,
  event_id UUID,
  FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE 
);