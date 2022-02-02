import React, { useState } from "react";
import {
  Container,
  Row,
  Form,
  Button,
  Alert,
  InputGroup,
  Spinner,
  CardColumns,
  Card,
} from "react-bootstrap";
import { searchArtworks } from "../../api";

function Homepage({ onLogout }) {
  const [isLoading, setIsLoading] = useState(false);
  const [noArtworksFound, setNoArtworksFound] = useState(false);
  const [keyword, setKeyword] = useState("");
  const [artworks, setArtworks] = useState([]);

  const onChangeKeyword = (event) => {
    setKeyword(event.target.value);
  };

  const onSearchArtworks = async (event) => {
    event.preventDefault();
    setIsLoading(true);
    const artworks = await searchArtworks({ keyword });
    setArtworks(artworks);
    setNoArtworksFound(!artworks || !artworks.length);
    setIsLoading(false);
  };

  return (
    <Container fluid>
      <Row className="mt-2 mb-2 justify-content-end" noGutters>
        <Button variant="outline-danger" onClick={onLogout}>
          Log out
        </Button>
      </Row>
      <Row noGutters>
        <h1>Welcome!</h1>
      </Row>
      <Row className="mt-2" noGutters>
        <h6>
          Enter one or multiple keywords below to search for artworks in the Art
          Institute of Chicago.
        </h6>
      </Row>
      <Row noGutters>
        <Form className="w-100 mb-5" onSubmit={onSearchArtworks}>
          <InputGroup>
            <Form.Control
              type="text"
              placeholder="e.g. Monet, O'Keeffe, Ancient Greek..."
              onChange={onChangeKeyword}
              value={keyword}
            />
            <InputGroup.Prepend>
              <Button
                variant="outline-primary"
                disabled={!keyword}
                type="submit"
              >
                Search artworks
              </Button>
            </InputGroup.Prepend>
          </InputGroup>
        </Form>
      </Row>
      {isLoading && (
        <Row className="justify-content-center mb-5">
          <Spinner animation="border" variant="primary" />
        </Row>
      )}
      
    </Container>
  );
}

export default Homepage;
