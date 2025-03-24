-- использует ли пользователь хотя бы один продукт типа DEBIT
SELECT COUNT(*)
FROM TRANSACTIONS t
         JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID
WHERE t.USER_ID = ?
  AND p.TYPE = 'DEBIT';