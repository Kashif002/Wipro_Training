/**
 * Simplified session validation to prevent back button access after logout
 */

(function() {
    'use strict';
    
    // Function to check if user has valid auth token
    function hasAuthToken() {
        const cookies = document.cookie.split(';');
        
        for (let cookie of cookies) {
            const trimmedCookie = cookie.trim();
            // Check for JWT tokens
            if (trimmedCookie.startsWith('jwt=') || trimmedCookie.startsWith('adminToken=')) {
                const tokenValue = trimmedCookie.split('=')[1];
                // Valid token if not empty or deleted
                if (tokenValue && tokenValue !== 'null' && tokenValue !== 'deleted' && tokenValue !== '') {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    
    // Simple initialization - only check when page loads from cache
    function init() {
        // Only run on admin pages
        if (window.location.pathname.startsWith('/admin/')) {
            // Check if page was loaded from back button (from cache)
            if (performance.navigation && performance.navigation.type === 2) {
                // Page was loaded from back/forward button
                if (!hasAuthToken()) {
                    // No valid token, redirect to login
                    window.location.replace('/login?sessionExpired=true');
                    return;
                }
            }
            
            // Simple back button handler
            window.addEventListener('pageshow', function(event) {
                if (event.persisted) {
                    // Page was loaded from cache
                    if (!hasAuthToken()) {
                        window.location.replace('/login?sessionExpired=true');
                    }
                }
            });
        }
    }
    
    // Run initialization
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }
    
})();
