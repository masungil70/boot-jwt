// Refresh Token을 사용하여 새로운 Access Token을 발급 받는 함수
const refreshToken = async () => {
    const refreshToken = localStorage.getItem('refreshToken');
    const accessToken = localStorage.getItem('accessToken');

    if (!refreshToken || !accessToken) {
        redirectToLogin();  // 리디렉션
        return Promise.reject('No tokens found');
    }

    try {
        const response = await fetch('http://localhost:8080/refreshToken', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ accessToken, refreshToken }),
        });

        const data = await response.json();

        if (response.ok) {
            // 새 토큰 저장
            const { accessToken: newAccessToken, refreshToken: newRefreshToken } = data;
            
            localStorage.setItem('accessToken', newAccessToken);
            localStorage.setItem('refreshToken', newRefreshToken);
            
            return newAccessToken;
        } else {
            // refreshToken이 만료된 경우 등
            redirectToLogin();
            return Promise.reject(data?.msg || 'Failed to refresh token');
        }
    } catch (error) {
        console.error('Token refresh failed', error);
        redirectToLogin();
        return Promise.reject(error);
    }
};

// 인증된 요청을 보내는 함수: 자동 토큰 갱신 및 재요청
const kosa_fetch = async (url, options = {}) => {
    let accessToken = localStorage.getItem('accessToken');

    if (accessToken) {
        options.headers = {
            ...options.headers,
            'Authorization': `Bearer ${accessToken}`,
        };
    }

    try {
        let response = await fetch(url, options);

        if (response.status === 403) {
            // accessToken이 만료됨: refresh 시도
            try {
                const newAccessToken = await refreshToken();
                if (newAccessToken) {
                    options.headers['Authorization'] = `Bearer ${newAccessToken}`;
                    response = await fetch(url, options); // 재시도
                }
            } catch (e) {
                console.warn('Refresh failed, redirecting to login...');
                return Promise.reject(e);
            }
        }

        if (!response.ok) {
            const errData = await response.json();
            return Promise.reject(errData);
        }

        return response.json();

    } catch (error) {
        console.error('Request failed:', error);
        return Promise.reject(error);
    }
};

// 로그인 페이지로 리디렉션 함수
const redirectToLogin = () => {
    // 토큰 제거
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    // 페이지 이동
    window.location.href = '/login.html';
};
