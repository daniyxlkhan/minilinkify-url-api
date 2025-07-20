# Google Cloud Run Deployment Guide

## Prerequisites

1. **Google Cloud Account** with billing enabled
2. **Google Cloud CLI** installed ([Download](https://cloud.google.com/sdk/docs/install))
3. **Docker** installed locally (for testing)

## Setup Steps

### 1. Enable Required APIs
```bash
# Enable required Google Cloud APIs
gcloud services enable run.googleapis.com
gcloud services enable cloudbuild.googleapis.com
gcloud services enable containerregistry.googleapis.com
gcloud services enable sql-component.googleapis.com
```

### 2. Create Google Cloud SQL PostgreSQL Database
```bash
# Create a PostgreSQL instance
gcloud sql instances create minilinkify-db \
  --database-version=POSTGRES_13 \
  --tier=db-f1-micro \
  --region=us-central1

# Create database
gcloud sql databases create minilinkify --instance=minilinkify-db

# Create database user
gcloud sql users create minilinkify-user \
  --instance=minilinkify-db \
  --password=your-secure-password
```

### 3. Deploy Application to Cloud Run

#### Option A: Using Google Cloud Build (Recommended)
```bash
# Deploy using Cloud Build
gcloud builds submit --config cloudbuild.yaml
```

#### Option B: Manual Docker Build and Deploy
```bash
# Set your project ID
export PROJECT_ID=your-project-id

# Build and push Docker image
docker build -t gcr.io/$PROJECT_ID/minilinkify-url-api .
docker push gcr.io/$PROJECT_ID/minilinkify-url-api

# Deploy to Cloud Run
gcloud run deploy minilinkify-url-api \
  --image gcr.io/$PROJECT_ID/minilinkify-url-api \
  --platform managed \
  --region us-central1 \
  --allow-unauthenticated \
  --port 8080 \
  --memory 1Gi \
  --cpu 1 \
  --max-instances 10
```

### 4. Configure Database Connection
```bash
# Get database connection name
gcloud sql instances describe minilinkify-db --format="value(connectionName)"

# Set environment variables for Cloud Run
gcloud run services update minilinkify-url-api \
  --region us-central1 \
  --set-env-vars="SPRING_DATASOURCE_URL=jdbc:postgresql://google/minilinkify?socketFactory=com.google.cloud.sql.postgres.SocketFactory&cloudSqlInstance=YOUR_CONNECTION_NAME" \
  --set-env-vars="SPRING_DATASOURCE_USERNAME=minilinkify-user" \
  --set-env-vars="SPRING_DATASOURCE_PASSWORD=your-secure-password"
```

### 5. Enable Cloud SQL Connections
```bash
# Update Cloud Run service to connect to Cloud SQL
gcloud run services update minilinkify-url-api \
  --region us-central1 \
  --add-cloudsql-instances YOUR_CONNECTION_NAME
```

## Configuration Details

### Environment Variables
- `SPRING_DATASOURCE_URL`: PostgreSQL connection string with Cloud SQL socket factory
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `PORT`: Application port (automatically set by Cloud Run to 8080)

### Resource Limits
- **Memory**: 1GB
- **CPU**: 1 core
- **Max Instances**: 10 (adjust based on needs)

## Testing Your Deployment

1. **Get service URL**:
   ```bash
   gcloud run services describe minilinkify-url-api \
     --region us-central1 \
     --format="value(status.url)"
   ```

2. **Health check**:
   ```bash
   curl https://your-service-url/actuator/health
   ```

3. **Test API**:
   ```bash
   curl -X POST https://your-service-url/api/url/shorten \
     -H "Content-Type: application/json" \
     -d '{"originalUrl": "https://example.com"}'
   ```

## Cost Optimization

### Free Tier Limits
- **Cloud Run**: 2 million requests per month
- **Cloud SQL**: db-f1-micro instance (shared CPU, 0.6 GB RAM)
- **Container Registry**: 0.5 GB storage

### Production Recommendations
- Use **Cloud SQL Proxy** for secure connections
- Enable **Cloud Monitoring** and **Cloud Logging**
- Set up **Custom Domain** and **SSL certificates**
- Consider **Cloud Load Balancer** for high availability

## Troubleshooting

### Common Issues
1. **Build fails**: Check Dockerfile and dependencies
2. **Database connection**: Verify Cloud SQL instance and credentials
3. **Service won't start**: Check logs with `gcloud run logs tail`
4. **Port issues**: Ensure application listens on port 8080

### View Logs
```bash
# View real-time logs
gcloud run logs tail minilinkify-url-api --region us-central1

# View specific log entries
gcloud logs read "resource.type=cloud_run_revision AND resource.labels.service_name=minilinkify-url-api"
```

## Cleanup
```bash
# Delete Cloud Run service
gcloud run services delete minilinkify-url-api --region us-central1

# Delete Cloud SQL instance
gcloud sql instances delete minilinkify-db
```

Your application is now ready for Google Cloud Run deployment! 🚀
