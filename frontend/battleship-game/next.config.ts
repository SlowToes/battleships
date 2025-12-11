const nextConfig = {
    images: {
        remotePatterns: [
            {
                protocol: 'https',
                hostname: 'www.cbc.ca',
                port: '',
                pathname: '/**',
            },
            {
                protocol: 'https',
                hostname: 'cdn.dribbble.com',
                port: '',
                pathname: '/**',
            },
        ],
    },
}

export default nextConfig;
