// admin-security.js
(function() {
    'use strict';
    
    // Disable back button navigation after logout
    function disableBackButton() {
        window.history.pushState(null, null, window.location.href);
        
        window.onpopstate = function() {
            window.history.go(1);
        };
    }
    
    // Check authentication status
    function validateSession() {
        fetch('/api/admin/validate-session', {
            method: 'GET',
            credentials: 'include'
        })
        .then(response => {
            if (!response.ok) {
                window.location.replace('/login?sessionExpired=true');
            }
        })
        .catch(() => {
            window.location.replace('/login?sessionExpired=true');
        });
    }
    
    // Initialize when DOM is ready
    document.addEventListener('DOMContentLoaded', function() {
        disableBackButton();
    });
    
    // Handle page visibility changes
    document.addEventListener('visibilitychange', function() {
        if (!document.hidden) {
            validateSession();
        }
    });
    
    // Handle page show event (back/forward cache)
    window.addEventListener('pageshow', function(event) {
        if (event.persisted) {
            disableBackButton();
        }
    });
})();
