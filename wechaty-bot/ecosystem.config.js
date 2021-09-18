module.exports = {
    apps: [{
        script: './src/index.js',
        watch: ['config', 'src'],
        name: 'wxBot',
        args: 'one two',
        instances: 1,
        exec_mode: "cluster",
        autorestart: true,
        max_memory_restart: '1G',
        log: './logs/wx-bot.log',
        log_date_format: "YYYY-MM-DD HH:mm Z", // pm2 log添加日期
        env: {
            NODE_ENV: 'development'
        },
        env_production: {
            NODE_ENV: 'production'
        }
    }],

    deploy: {
        production: {
            user: 'SSH_USERNAME',
            host: 'SSH_HOSTMACHINE',
            ref: 'origin/master',
            repo: 'GIT_REPOSITORY',
            path: 'DESTINATION_PATH',
            'pre-deploy-local': '',
            'post-deploy': 'npm install && pm2 reload ecosystem.config.js --env production',
            'pre-setup': ''
        }
    }
};
