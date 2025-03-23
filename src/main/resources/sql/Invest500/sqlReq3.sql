-- Сумма пополнений продуктов с типом SAVING больше 1000 ₽
SELECT COALESCE(SUM(t.AMOUNT), 0)
FROM TRANSACTIONS t
         JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID
WHERE t.USER_ID = ? AND p.TYPE = 'SAVING' AND t.TYPE = 'DEPOSIT';