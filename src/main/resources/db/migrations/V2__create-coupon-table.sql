CREATE TABLE coupon (
  id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
  code VARCHAR(50) NOT NULL,
  discount INTEGER NOT NULL,
  expiration_date DATE NOT NULL,
  event_id UUID,
  FOREIGN KEY (event_id) REFERENCES event(id) ON DELETE CASCADE 
);