### **📌 Future Automation Plan for GarminDB & HRVibe Integration**

Since we're focusing on **manual syncing first**, here’s a **saved summary** of automation options for later. Each option includes **estimated effort** so we can plan accordingly.

---

## **1️⃣ Automating GarminDB Data Fetching (Python)**
### **❌ Current Process (Manual)**
- Manually run the GarminDB script:
  ```bash
  python3 /path/to/GarminDB/download.py
  ```
- This fetches **all** data from 2024–2025, even if already fetched.

---

### **✅ Future Options for Automating GarminDB Fetching**
| **Option** | **How It Works?** | **Pros** | **Cons** | **Estimated Effort** |
|------------|------------------|----------|----------|-----------------|
| **A. Add a Cron Job (Easiest)** | Set up a cron job to run the script once per day (on local machine or server). | ✅ Simple, no code changes. ✅ Works even if HRVibe is not running. | ❌ Still runs GarminDB separately. ❌ Needs manual setup on deployment. | **⏳ ~30 min (basic setup)** |
| **B. Modify Python Script to Fetch Only New Data** | Change `download.py` to accept a `--start-date` argument and only fetch new records. | ✅ Faster, avoids redundant data fetching. ✅ Reduces SQLite file size. | ❌ Requires modifying GarminDB. ❌ Needs logic to track last fetched date. | **⏳ ~2–3 hours (Python changes & testing)** |
| **C. Trigger GarminDB Fetch from Java** | Expose an API in HRVibe (`POST /api/fetch-garmin`) that runs `python3 download.py`. | ✅ HRVibe controls the fetching. ✅ Easier to manage when deployed. | ❌ More complex. ❌ Java needs permission to execute Python. | **⏳ ~4–5 hours (API, Java-Python execution, testing)** |

🔹 **Best Starting Point:** **Option A (Cron Job)** → Quick & easy.  
🔹 **Best Long-Term Solution:** **Combine B (Fetch only new data) + C (Trigger from Java).**

---

## **2️⃣ Automating SQLite → MongoDB Sync (Java)**
### **❌ Current Process (Manual)**
1. Run SQL queries **manually** to fetch new data.
2. Manually insert into MongoDB using `repository.save()`.
3. No automatic updates—data **only updates when manually triggered**.

---

### **✅ Future Options for Automating SQL → MongoDB Sync**
| **Option** | **How It Works?** | **Pros** | **Cons** | **Estimated Effort** |
|------------|------------------|----------|----------|-----------------|
| **A. Add a Scheduled Job in Java (Best for MVP)** | Use `@Scheduled(cron = "0 0 1 * * ?")` to run SQL → MongoDB sync every night at 1 AM. | ✅ Fully automatic once deployed. ✅ No manual intervention. | ❌ Requires HRVibe to be running. ❌ If HRVibe is down, sync won’t run. | **⏳ ~1–2 hours (Spring Scheduler)** |
| **B. Trigger Sync via API (Flexible Approach)** | Create `POST /api/sync-database` that runs SQL sync when called. | ✅ Can be triggered anytime. ✅ Works whether deployed or local. | ❌ Still requires manual API call if not automated. | **⏳ ~1–2 hours (Spring API & SQL queries)** |
| **C. Combine Scheduled Job + API Trigger (Best for Production)** | HRVibe syncs automatically **every night**, but also has a `POST /api/sync-database` to allow manual syncing. | ✅ Best of both worlds—automatic but still controllable. ✅ Prevents downtime issues. | ❌ More logic needed to track last sync date. | **⏳ ~3–4 hours (API + Scheduler + Tracking Last Sync Date)** |

🔹 **Best Starting Point:** **Option B (API trigger first)** → Allows testing before automation.  
🔹 **Best Long-Term Solution:** **Option C (Scheduled + API Trigger).**

---

## **🔥 Summary: Best Approach for MVP & Later Automation**
| **Task** | **MVP Approach** | **Future Automation** |
|----------|----------------|------------------|
| **Fetching GarminDB Data** | Run manually. | Option A (Cron Job) → Option C (Trigger from Java). |
| **Fetching Only New Data** | Fetch all data every time. | Modify Python script to fetch only new data. |
| **Syncing SQLite → MongoDB** | Manually run SQL queries & insert into MongoDB. | Option B (API trigger) → Option C (Scheduled + API). |

---

## **📌 Next Steps**
1. **Save this plan for after MVP.**
2. **Focus on backend integration now.**
3. **After MVP, revisit this and start automation with simple cron jobs.**
