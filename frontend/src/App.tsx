import { useState } from 'react';

interface Article {
  title: string;
  url: string;
}

function App() {
    const [topic, setTopic] = useState('');
    const [liveTopic, setLiveTopic] = useState('');
    const [liveArticles, setLiveArticles] = useState<Article[]>([]);
    const [deferredArticles, setDeferredArticles] = useState<Record<string, Article[]>>({});
    const [errorMessage, setErrorMessage] = useState('');

  const handleFetchNews = async () => {
    setErrorMessage('');
    setLiveTopic(topic); // Save current topic for display

    try {
      const response = await fetch(`http://localhost:8080/news?topic=${topic}`);

      if (response.status === 429) {
            const errorJson = await response.json();
            setErrorMessage(`⚠️ ${errorJson.error}`);
            setLiveArticles([]);
            return;
      }

      const data = await response.json();
      if (data.status === 'ok') {
        const articles: Article[] = data.articles.slice(0, 5).map((a: any) => ({
          title: a.title,
          url: a.url,
        }));
        setLiveArticles(articles);
      }
    } catch (err) {
      setErrorMessage('❌ Failed to fetch news.');
    }
  };


  const handleFetchDeferred = async () => {
    const response = await fetch('http://localhost:8080/news/deferred');
    const data = await response.json();

    const parsed: Record<string, Article[]> = {};
    Object.entries(data).forEach(([key, value]) => {
      try {
        const parsedValue = JSON.parse(value as string);
        if (parsedValue.status === 'ok') {
          parsed[key] = parsedValue.articles.slice(0, 5).map((a: any) => ({
            title: a.title,
            url: a.url
          }));
        }
      } catch (e) {
        console.error(`Could not parse deferred value for ${key}`, e);
      }
    });

    setDeferredArticles(parsed);
  };

  return (
    <div style={{ padding: '20px', fontFamily: 'Arial' }}>
      <h1>📰 News Feed Aggregator</h1>

      <input
        type="text"
        placeholder="Enter topic"
        value={topic}
        onChange={(e) => setTopic(e.target.value)}
        style={{ padding: '8px', width: '300px' }}
      />
      <button onClick={handleFetchNews} style={{ marginLeft: '10px', padding: '8px' }}>
        Get News
      </button>
      <button onClick={handleFetchDeferred} style={{ marginLeft: '10px', padding: '8px' }}>
        Show Deferred News
      </button>

      <hr />

      <h2>🔍 Live News for: <em>{liveTopic}</em></h2>
      {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
      <ul>
        {liveArticles.map((article, index) => (
          <li key={index}>
            <a href={article.url} target="_blank" rel="noopener noreferrer">
              {article.title}
            </a>
          </li>
        ))}
      </ul>


      <h2>🕓 Deferred News</h2>
      {Object.entries(deferredArticles).map(([key, articles]) => (
        <div key={key} style={{ marginBottom: '20px' }}>
          <h4>{key}</h4>
          <ul>
            {articles.map((article, idx) => (
              <li key={idx}>
                <a href={article.url} target="_blank" rel="noopener noreferrer">
                  {article.title}
                </a>
              </li>
            ))}
          </ul>
        </div>
      ))}
    </div>
  );
}

export default App;
