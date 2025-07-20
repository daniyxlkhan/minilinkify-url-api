# Render Deployment Troubleshooting

## Issues Fixed in This Update

### 1. Changed from Docker to Native Java Runtime
- **Problem**: Docker build was failing with file permission issues
- **Solution**: Changed `render.yaml` to use `env: java` instead of `runtime: docker`
- **Benefit**: Faster builds, better compatibility with Render's free tier

### 2. Improved Build Script
- **Enhanced error handling and logging**
- **Better JAR file detection logic**
- **Java version checking**
- **Verbose output for debugging**

### 3. Updated Maven Configuration
- **Fixed Maven wrapper properties**
- **Updated to Maven 3.9.6**
- **Better file permissions handling**

## Current Configuration

### render.yaml
```yaml
services:
  - type: web
    name: minilinkify-api
    env: java                    # ✅ Native Java (not Docker)
    plan: free
    buildCommand: ./build.sh
    startCommand: ./start.sh
    envVars:
      - key: PORT
        value: 8080
      - key: JAVA_OPTS
        value: "-Xmx400m -XX:MaxMetaspaceSize=100m"
    healthCheckPath: /actuator/health

databases:
  - name: minilinkify-db
    databaseName: minilinkify
    plan: free
```

## Deployment Steps (Updated)

### 1. Commit and Push Changes
```bash
git add .
git commit -m "Fix Render deployment configuration"
git push origin main
```

### 2. Redeploy on Render
- Go to your Render service
- Click "Manual Deploy" → "Deploy latest commit"
- Or delete the old service and create a new Blueprint

### 3. Monitor Build Logs
Watch for these success indicators:
- ✅ "Using Maven wrapper..."
- ✅ "Build completed, looking for JAR files..."
- ✅ "Found and copied: [jar-name]"
- ✅ "Build successful! app.jar created"

## Common Issues and Solutions

### Build Fails with "No jar file found"
**Check**: Maven dependencies and build process
**Solution**: Look at the Maven output for compilation errors

### Application Won't Start
**Check**: 
- Database connection (DATABASE_URL environment variable)
- Port configuration (should be automatically set by Render)
- Java heap size (current: 400MB for free tier)

### Health Check Fails
**Check**: 
- Spring Boot Actuator dependency is included
- `/actuator/health` endpoint is accessible
- Application is fully started (may take 2-3 minutes)

### Database Connection Issues
**Ensure**:
- PostgreSQL database is created in Render
- DATABASE_URL is automatically connected
- Application properties use the correct environment variable

## Environment Variables (Auto-configured)
- `PORT` - Automatically set by Render
- `DATABASE_URL` - Automatically set when database is connected
- `JAVA_OPTS` - Set to optimize for free tier

## Support
If issues persist:
1. Check Render service logs
2. Verify all files are committed to Git
3. Ensure scripts have execute permissions
4. Consider upgrading to paid plan for more resources

## Success Indicators
When deployment works, you should see:
- Build completes without errors
- Health check returns 200 OK
- Application responds to API requests
- Database tables are created automatically
