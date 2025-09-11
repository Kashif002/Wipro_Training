// Admin Service Utility Functions
const AdminUtils = {
    // Format currency for Indian banking
    formatCurrency: (amount) => {
        return new Intl.NumberFormat('en-IN', {
            style: 'currency',
            currency: 'INR',
            minimumFractionDigits: 2
        }).format(amount);
    },

    // Format date and time for admin interface
    formatDateTime: (timestamp) => {
        return new Date(timestamp).toLocaleString('en-IN', {
            year: 'numeric',
            month: 'short',
            day: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    },

    // Make authenticated API calls with JWT token
    makeApiCall: async (url, method = 'GET', data = null) => {
        // FIXED: Use sessionStorage instead of localStorage
        const token = sessionStorage.getItem('adminToken');
        const options = {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            }
        };

        // Add authorization header if token exists
        if (token) {
            options.headers['Authorization'] = `Bearer ${token}`;
        }

        // Add request body for POST/PUT requests
        if (data && (method === 'POST' || method === 'PUT')) {
            options.body = JSON.stringify(data);
        }

        try {
            const response = await fetch(url, options);
            
            // Handle unauthorized responses
            if (response.status === 401) {
                AdminUtils.redirectToLogin();
                return null;
            }

            // Handle error responses
            if (!response.ok) {
                const errorText = await response.text();
                console.error('API call failed:', response.status, errorText);
                throw new Error(`HTTP ${response.status}: ${errorText}`);
            }

            return await response.json();
        } catch (error) {
            console.error('API call failed:', error);
            AdminUtils.showAlert('danger', 'Network error: ' + error.message);
            throw error; // Re-throw to let calling code handle it
        }
    },

    // Show alert messages to admin
    showAlert: (type, message) => {
        const alertContainer = document.getElementById('alert-container');
        if (!alertContainer) return;

        const alertDiv = document.createElement('div');
        alertDiv.className = `alert alert-${type}`;
        // Use innerHTML to support HTML content like <br> tags
        alertDiv.innerHTML = `
            <div class="alert-message">${message}</div>
            <button class="btn-close" onclick="this.parentElement.remove()">&times;</button>
        `;

        alertContainer.appendChild(alertDiv);

        // Auto-remove alert after 5 seconds
        setTimeout(() => {
            if (alertDiv.parentElement) {
                alertDiv.remove();
            }
        }, 5000);
    },

    // Redirect to login page
    redirectToLogin: () => {
        // FIXED: Clear sessionStorage instead of localStorage
        sessionStorage.removeItem('adminToken');
        window.location.href = '/login';
    },

    // Check if admin is authenticated
    isAuthenticated: () => {
        // Check sessionStorage first (for backward compatibility)
        const sessionToken = sessionStorage.getItem('adminToken');
        if (sessionToken && sessionToken !== 'null' && sessionToken !== '') {
            return true;
        }
        
        // Check if JWT cookie exists (primary method)
        const cookieToken = AdminUtils.getCookie('adminToken') || AdminUtils.getCookie('jwt');
        if (cookieToken && cookieToken !== 'null' && cookieToken !== '') {
            return true;
        }
        
        return false;
    },
    
    // Helper method to get cookie value
    getCookie: (name) => {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) {
            return parts.pop().split(';').shift();
        }
        return null;
    },

    // Validate email format
    isValidEmail: (email) => {
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailRegex.test(email);
    },

    // Show loading spinner
    showLoading: (elementId) => {
        const element = document.getElementById(elementId);
        if (element) {
            element.innerHTML = '<div class="loading"></div> Loading...';
        }
    },

    // Hide loading spinner
    hideLoading: (elementId, originalContent = '') => {
        const element = document.getElementById(elementId);
        if (element) {
            element.innerHTML = originalContent;
        }
    },

    // Confirm dialog for admin actions
    confirmAction: (message) => {
        return confirm(`Are you sure you want to ${message}?`);
    },

    // Chat API endpoints
    async getUnreadCustomers() {
        return this.makeApiCall('/admin/chat/api/unread-customers');
    },

    async getConversation(customerId) {
        return this.makeApiCall(`/admin/chat/api/conversation/${customerId}`);
    },

    async sendMessage(customerId, message) {
        return this.makeApiCall('/admin/chat/api/send', 'POST', {
            customerId: customerId,
            message: message
        });
    },

    async markConversationAsRead(customerId) {
        return this.makeApiCall(`/admin/chat/api/mark-read/${customerId}`, 'POST');
    }
};

// Export for use in other files
if (typeof module !== 'undefined' && module.exports) {
    module.exports = AdminUtils;
}
