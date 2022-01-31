import React from "react";
import { Button, Container, Grid, Header, Input } from "semantic-ui-react";
import "./App.css";

const App = () => {
  const [user, setUser] = React.useState({});
  const [amount, setAmount] = React.useState(0);
  const [search, setSearch] = React.useState("");
  const [buyAmount, setBuyAmount] = React.useState(0);
  const [searching, setSearching] = React.useState(false);
  const [buying, setBuying] = React.useState(false);
  const [selling, setSelling] = React.useState(false);

  const [searchResult, setSearchResult] = React.useState();

  const addBalance = () => {
    const requestOptions = {
      method: "PUT",
    };
    fetch(
      "/api/portfolio/add-balance/" +
        user.id +
        "/" +
        amount,
      requestOptions
    ).then(() => {
      getUser(user.id);
      alert("Balance Added");
      setAmount(0);
    });
  };
  const searchStock = () => {
    setSearching(true);
    const requestOptions = {
      method: "POST",
    };
    fetch(
      "/api/portfolio/get-stock/" + search,
      requestOptions
    )
      .then((response) => response.json())
      .then((data) => {
        setSearchResult(data);
      })
      .finally(() => {
        setSearching(false);
      });
  };
  const buyStock = () => {
    setBuying(true);
    const requestOptions = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        quantity: buyAmount,
        stock: {
          symbol: searchResult.symbol,
          name: searchResult.name,
          currency: searchResult.currency,
          stockExchange: searchResult.stockExchange,
          price: searchResult.quote.price,
        },
      }),
    };
    fetch(
      "/api/portfolio/buy-stock/" + user.id,
      requestOptions
    )
      .then((response) => response.json())
      .then(() => {
        getUser(user.id);
        alert("Stock buying complete");
      })
      .catch(() => {
        alert("Error buying Stock");
        getUser(user.id);
      })
      .finally(() => {
        setBuying(false);
      });
  };

  const sellStock = (index) => {
    setSelling(true);
    const requestOptions = {
      method: "POST",
      headers: { "Content-Type": "application/json" },
    };
    fetch(
      "/api/portfolio/sell-stock/" + user.id + "/" + index,
      requestOptions
    )
      .then(() => {
        getUser(user.id);
        alert("Stock Sold");
      })
      .catch(() => {
        getUser(user.id);
        alert("Error selling Stock");
      })
      .finally(() => {
        setSelling(false);
      });
  };

  const getUser = (id) => {
    const requestOptions = {
      method: "GET",
    };
    fetch("/api/portfolio/" + id, requestOptions)
      .then((response) => response.json())
      .then((data) => setUser(data));
  };
  React.useEffect(() => {
    getUser(1);
  }, []);
  return (
    <div className="App">
      <Container>
        <Grid columns={2} padded>
          <Grid.Row>
            <Grid.Column width={8}>
              <React.Fragment>
                <Grid.Row style={{ minHeight: "100px" }}>
                  <Grid.Column width={8}>
                    <div style={{ float: "left" }}>
                      <Header as="h3">User Name: {user.name}</Header>
                      <Header as="h3">Balance: {user.balance}</Header>
                    </div>
                    <div style={{ float: "right", paddingTop: "15px" }}>
                      <Input
                        type="number"
                        value={amount}
                        onChange={(data) => {
                          setAmount(data.target.value);
                        }}
                      ></Input>
                      <Button
                        style={{ marginLeft: "10px" }}
                        onClick={addBalance}
                        disabled={amount <= 0}
                      >
                        Add Balance
                      </Button>
                    </div>
                  </Grid.Column>
                </Grid.Row>
                <Grid.Row>
                  <hr></hr>
                  <Grid.Column width={6}>
                    <Input
                      placeholder="Search for a stock"
                      onChange={(data) => {
                        setSearch(data.target.value);
                      }}
                    ></Input>
                    <Button
                      style={{ marginLeft: "10px" }}
                      onClick={searchStock}
                      disabled={searching || search.length === 0}
                    >
                      Search
                    </Button>
                  </Grid.Column>
                </Grid.Row>
                <Grid.Row>
                  <Grid.Column width={6}>
                    {searchResult && (
                      <>
                        <div>Symbol : {searchResult.symbol}</div>
                        <div>Name : {searchResult.name}</div>
                        <div>Currency : {searchResult.currency}</div>
                        <div>StockExchange : {searchResult.stockExchange}</div>
                        <div>Price : {searchResult.quote.price}</div>
                      </>
                    )}
                  </Grid.Column>
                  {searchResult && (
                    <Grid.Column width={6}>
                      <Input
                        placeholder="Buy Quantity"
                        type="number"
                        min="1"
                        value={buyAmount}
                        onChange={(data) => {
                          setBuyAmount(data.target.value);
                        }}
                      ></Input>
                      <Button
                        style={{ marginLeft: "10px" }}
                        onClick={buyStock}
                        disabled={
                          buyAmount === null ||
                          buyAmount === undefined ||
                          buyAmount <= 0 ||
                          buying
                        }
                      >
                        Buy
                      </Button>
                    </Grid.Column>
                  )}
                </Grid.Row>
              </React.Fragment>
            </Grid.Column>
            <Grid.Column width={8}>
              {user?.boughtStock?.boughtStockDetails.length > 0 && (
                <Header as="h4">Bought Stock Details:</Header>
              )}
              {user?.boughtStock?.boughtStockDetails?.map((data, index) => {
                return (
                  <div>
                    <Button
                      style={{ float: "right" }}
                      disabled={selling}
                      onClick={() => {
                        sellStock(index);
                      }}
                    >
                      Sell
                    </Button>
                    <div>Stock Name: {data.stock.name}</div>
                    <div>Price: {data.stock.price}</div>
                    <div>Quantity: {data.quantity}</div>
                    <div>Currency: {data.stock.currency}</div>
                    <div>StockExchange: {data.stock.stockExchange}</div>
                    <div>Symbol: {data.stock.symbol}</div>
                    <hr />
                  </div>
                );
              })}
            </Grid.Column>
          </Grid.Row>
        </Grid>
      </Container>
    </div>
  );
};

export default App;
