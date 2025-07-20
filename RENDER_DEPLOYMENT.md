# Render Deployment Guide for Minilinkify URL API

## Prerequisites
1. A Render account (sign up at [render.com](https://render.com/))
2. Your project pushed to a Git repository (GitHub, GitLab, or Bitbucket)
3. Java 17 (for local testing)

## Quick Deployment (Recommended)

### Step 1: Push to Git Repository
```bash
# Initialize git if not already done
git init
git add .
git commit -m "Prepare for Render deployment"

# Push to your remote repository (GitHub/GitLab/Bitbucket)
git remote add origin YOUR_REPOSITORY_URL
git push -u origin main
```

### Step 2: Deploy with Blueprint (render.yaml)
1. Go to [Render Dashboard](https://dashboard.render.com/)
2. Click "New +" and select "Blueprint"
3. Connect your Git repository
4. Render will detect the `render.yaml` file and create:
   - PostgreSQL database (`minilinkify-db`)
   - Web service (`minilinkify-api`)
5. Click "Apply" to start deployment

### Step 3: Configure Environment Variables
After deployment, the database URL will be automatically connected. No manual configuration needed!

### Option 2: Manual Setup

#### Step 1: Create PostgreSQL Database
1. Go to Render Dashboard
2. Click "New +" → "PostgreSQL"
3. Choose a name (e.g., `minilinkify-db`)
4. Select the Free plan
5. Click "Create Database"
6. Note down the connection details (you'll need them for the web service)

#### Step 2: Create Web Service
1. Click "New +" → "Web Service"
2. Connect your repository
3. Configure the service:
   - **Name**: `minilinkify-api`
   - **Runtime**: Docker
   - **Build Command**: `./build.sh`
   - **Start Command**: `./start.sh`
   - **Plan**: Free

#### Step 3: Set Environment Variables
In the web service settings, add these environment variables:
- `DATABASE_URL`: (Copy from your PostgreSQL database "External Database URL")
- `PORT`: `8080` (Render will override this automatically)
- `JAVA_OPTS`: `-Xmx512m` (Optional, for memory optimization)

## Important Notes

### Database Connection
- The application is configured to use `DATABASE_URL` environment variable
- Render automatically provides this when you create a PostgreSQL database
- The format should be: `postgresql://username:password@hostname:port/database`

### Health Checks
- The application includes Spring Boot Actuator for health monitoring
- Health check endpoint: `/actuator/health`
- Render will use this to monitor your application

### Build Process
- The `build.sh` script compiles your Java application using Maven
- The `start.sh` script runs the application
- Docker is used as the runtime environment

### Free Tier Limitations
- Free web services sleep after 15 minutes of inactivity
- Free databases have storage and connection limits
- For production use, consider upgrading to paid plans

## Testing Your Deployment

After deployment completes (usually 5-10 minutes), you can test your API:

### 1. Health Check
```bash
curl https://your-service-name.onrender.com/actuator/health
```

### 2. Create Short URL
```bash
curl -X POST https://your-service-name.onrender.com/api/url/shorten \
  -H "Content-Type: application/json" \
  -d '{"originalUrl": "https://example.com"}'
```

### 3. Access Short URL
Visit the returned short URL in your browser to test redirection.

### 4. Get URL Statistics
```bash
curl https://your-service-name.onrender.com/api/url/stats/YOUR_SHORT_CODE
```

Replace `your-service-name` with your actual Render service name.

## Troubleshooting

### Common Issues:
1. **Build fails**: Check Maven dependencies and Java version
2. **Database connection issues**: Verify DATABASE_URL environment variable
3. **Service won't start**: Check application logs in Render dashboard
4. **Health check fails**: Ensure `/actuator/health` endpoint is accessible

### Logs:
- View logs in the Render dashboard under your service
- Logs show build output and application runtime information

## Environment Variables Reference

| Variable | Description | Example |
|----------|-------------|---------|
| `DATABASE_URL` | PostgreSQL connection string | `postgresql://user:pass@host:port/db` |
| `PORT` | Server port (auto-managed by Render) | `8080` |
| `JAVA_OPTS` | JVM options | `-Xmx512m` |

## Next Steps
1. Set up custom domain (requires paid plan)
2. Configure SSL certificate (automatic on Render)
3. Set up monitoring and alerts
4. Consider upgrading to paid plans for production use
