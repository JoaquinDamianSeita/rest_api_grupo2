-- Roles
INSERT INTO roles (id, name) VALUES
                                 (1, 'BUYER'),
                                 (2, 'ARTIST');
-- Users
INSERT INTO users (
    id, username, email, password, role_id,
    first_name, last_name, address, registration_date, biography
) VALUES
      (1, 'alice', 'alice@example.com', 'pass123', 1,
       'Alice', 'Smith', '123 Main St, Cityville', NOW(), 'Digital art enthusiast and collector of rare tokens'),

      (2, 'bob', 'bob@example.com', 'pass456', 2,
       'Bob', 'Johnson', '456 Art Lane, Paintown', NOW(), 'Painter and 3D artist working with NFTs');

-- NFT Tokens
-- Insert for NFTToken
INSERT INTO nft_tokens (
    id, title, description, price, release_date, art_type, physical_pieces, available, user_id
) VALUES
    (1, 'Digital Art Piece', 'A unique digital art piece', 500.00, '2023-10-01 12:00:00', 'DIGITAL', 1, true, 2);

-- Insert for ImageUrl
INSERT INTO image_urls (
    id, url, nft_token_id
) VALUES
      (1, 'http://example.com/image1.png', 1),
      (2, 'http://example.com/image2.png', 1);

-- Insert for Sale
INSERT INTO sales (
    id, user_id, sale_date
) VALUES
      (1, 1, '2023-10-01 12:00:00');

-- Sale Tokens
-- Insert for SaleToken
INSERT INTO sale_tokens (
    sale_id, token_id, sale_price
) VALUES
      (1, 1, 450.00);
