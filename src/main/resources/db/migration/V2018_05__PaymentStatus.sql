UPDATE TEAM SET payment_status = 'COMPLETED' WHERE payment_status = 'Completed';
UPDATE TEAM SET payment_status = 'PENDING' WHERE payment_status IS NULL;