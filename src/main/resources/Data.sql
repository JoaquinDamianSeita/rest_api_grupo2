/*-- Users
INSERT INTO users (
    id, username, email, password, role,
    first_name, last_name, address, registration_date, biography
) VALUES
      (1, 'alice', 'alice@example.com', 'pass123', 'BUYER',
       'Alice', 'Smith', '123 Main St, Cityville', NOW(), 'Digital art enthusiast and collector of rare tokens'),

      (2, 'bob', 'bob@example.com', 'pass456', 'ARTIST',
       'Bob', 'Johnson', '456 Art Lane, Paintown', NOW(), 'Painter and 3D artist working with NFTs');


-- NFT Tokens
INSERT INTO nft_tokens (id, user_id, title, description, price, release_date, art_type, physical_pieces, sold) VALUES
                                                                                                                   (1, 1, 'Mona Lisa NFT', 'Digital version of Mona Lisa', 1500.00, NOW(), 'PAINTING', 5, false),
                                                                                                                   (2, 2, 'Space Odyssey', 'NFT from space art collection', 800.00, NOW(), 'DIGITAL', 0, true);

-- Image URLs
INSERT INTO image_urls (id, nft_token_id, url) VALUES
                                                   (1, 1, 'https://example.com/mona1.jpg'),
                                                   (2, 1, 'https://example.com/mona2.jpg'),
                                                   (3, 2, 'https://example.com/space1.jpg');

-- Carts
INSERT INTO carts (id, user_id, created_at) VALUES
                                    (1, 1, NOW()),
                                    (2, 2, NOW());

-- Cart Tokens
INSERT INTO cart_tokens (cart_id, token_id) VALUES
                                                (1, 1),
                                                (2, 2);

-- Sales
INSERT INTO sales (id, user_id, sale_date) VALUES
    (1, 1, NOW());

-- Sale Tokens
INSERT INTO sale_tokens (sale_id, token_id, sale_price) VALUES
    (1, 2, 750.00);
*/