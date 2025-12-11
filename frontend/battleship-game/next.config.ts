const nextConfig = {
    images: {
        remotePatterns: [
            {
                protocol: 'https',
                hostname: 'www.cbc.ca',
                port: '',
                pathname: '/**',
            },
        ],
    },
}

export default nextConfig;
