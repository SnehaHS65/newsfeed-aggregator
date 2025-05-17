# 📰 News Feed Aggregator — React + Spring Boot

A full-stack web application that fetches and displays live news articles based on a user-provided topic, with rate limiting and deferred request handling.

---

## 📌 Features

- 🔍 **Live News Fetching:** Users can search for real-time news by topic using NewsAPI.
- 🚦 **Rate Limiting:** Each user can make up to 5 API calls per minute. Excess requests are deferred.
- 💾 **Deferred Request Caching:** Extra requests are cached (using Caffeine) and processed in the background.
- ⏳ **Scheduled Processing:** Cached requests are processed every minute via a scheduled background job.
- ⚠️ **Frontend Feedback:** UI displays errors, warnings, and results in real-time.
- 🌈 **Modern UI:** Built with React and TypeScript (Vite).

---

## 🧰 Tech Stack

| Layer     | Tools & Libraries                          |
|-----------|---------------------------------------------|
| Frontend  | React (Vite, TypeScript), Fetch API         |
| Backend   | Spring Boot, Spring Cache, Bucket4j         |
| Caching   | Caffeine (in-memory), with optional Redis   |
| News API  | [NewsAPI.org](https://newsapi.org)          |
| Scheduler | Spring `@Scheduled` task                    |

---

## 📷 Screenshots


---
