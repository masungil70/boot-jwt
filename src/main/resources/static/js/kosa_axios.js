(function () {
	if (typeof axios === 'undefined') {
		console.error('Axios가 먼저 로드되어야 합니다. CDN을 먼저 포함하세요.');
		return;
	}

	// Axios 인스턴스 생성
	const kosa_axios = axios.create({
	    baseURL: 'http://localhost:8080',  // 기본 API 주소 설정
	    headers: {
	        'Content-Type': 'application/json',
	    }
	});
	
	// ✅ 로그인 페이지로 리디렉션 함수
	const redirectToLogin = () => {
	    localStorage.removeItem('accessToken');
	    localStorage.removeItem('refreshToken');
	    window.location.replace('/login.html');
	};
	
	
	// Refresh Token을 사용하여 새로운 Access Token을 발급 받는 함수
	const refreshToken = async () => {
	    const refreshToken = localStorage.getItem('refreshToken');
	    const accessToken = localStorage.getItem('accessToken');
	
	    // Refresh Token이 없으면 로그인 페이지로 리다이렉션 (토큰 갱신 불가)
	    if (!refreshToken || !accessToken) {
			redirectToLogin();
	        return Promise.reject('No tokens found');
	    }
	
	    try {
	        const response = await axios.post('/refreshToken', { accessToken, refreshToken });
	        // 새 토큰 저장
	        const { accessToken: newAccessToken, refreshToken: newRefreshToken } = response.data;
	        
	        localStorage.setItem('accessToken', newAccessToken);
	        localStorage.setItem('refreshToken', newRefreshToken);
	        
	        return newAccessToken;
	    } catch (error) {
	        console.error('Failed to refresh token', error);
	        redirectToLogin(); // refresh 실패 시 리디렉션
	        return Promise.reject(error);
	    }
	};
	
	// 요청 인터셉터: 요청이 보내기 전에 실행
	kosa_axios.interceptors.request.use(
	    (config) => {
	        const accessToken = localStorage.getItem('accessToken');
	        if (accessToken) {
	            config.headers['Authorization'] = `Bearer ${accessToken}`;
	        }
	        return config;
	    },
	    (error) => {
	        return Promise.reject(error);
	    }
	);
	
	// 응답 인터셉터: 응답을 받은 후 실행 (토큰 만료 에러 처리)
	kosa_axios.interceptors.response.use(
	    (response) => {
	        return response;
	    },
	    async (error) => {
	        const originalRequest = error.config;

	        // 403 에러 (Unauthorized) 발생 시
	        if (error.response && error.response.status === 403 && !originalRequest._retry) {
	            originalRequest._retry = true; // 무한 루프 방지
	
	            try {
	                // Refresh Token으로 새로운 Access Token 발급
	                const newAccessToken = await refreshToken();
	
	                if (newAccessToken) {
	                    // 기존 요청에 새로운 토큰을 추가하여 재시도
	                    originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
	                    return kosa_axios(originalRequest);  // 이전 요청을 재시도
	                }
	            } catch (refreshError) {
	                console.error('Token refresh failed', refreshError);
	                // 토큰 갱신에 실패하면 로그인 페이지로 리디렉션하거나 적절한 처리를 할 수 있습니다.
	                redirectToLogin();
	                return Promise.reject(refreshError);
	            }
	        }
	
	        // 그 외의 에러는 그대로 전달
	        return Promise.reject(error);
	    }
	);
	
 	// 전역으로 노출 (window에 할당)
  	window.kosa_axios = kosa_axios;
})();