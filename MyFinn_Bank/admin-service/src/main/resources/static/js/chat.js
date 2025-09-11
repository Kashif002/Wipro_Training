// Admin Chat JavaScript
let currentCustomerId = null;
let chatRefreshInterval = null;

document.addEventListener('DOMContentLoaded', function() {
    initializeChat();
    setupChatEventListeners();
    loadCustomersWithMessages();
});

// Initialize chat interface
function initializeChat() {
    // Get customer ID from URL if specified
    const urlParams = new URLSearchParams(window.location.search);
    const customerId = urlParams.get('customer');
    
    if (customerId) {
        selectCustomer(parseInt(customerId));
    }
    
    // Start auto-refresh for chat messages
    startChatRefresh();
}

// Set up chat event listeners
function setupChatEventListeners() {
    // Send message form
    const chatForm = document.getElementById('chat-form');
    if (chatForm) {
        chatForm.addEventListener('submit', sendMessage);
    }
    
    // Customer selection
    document.addEventListener('click', function(e) {
        if (e.target.classList.contains('customer-chat-item')) {
            const customerId = parseInt(e.target.dataset.customerId);
            selectCustomer(customerId);
        }
    });
    
    // Message input auto-resize
    const messageInput = document.getElementById('message-input');
    if (messageInput) {
        messageInput.addEventListener('input', autoResizeTextarea);
    }
}

// Load customers with chat messages
async function loadCustomersWithMessages() {
    try {
        const response = await AdminUtils.makeApiCall('/admin/chat/api/unread-customers');
        if (response) {
            updateCustomersList(response.customers);
            updateUnreadCount(response.totalUnread);
        }
    } catch (error) {
        AdminUtils.showAlert('danger', 'Failed to load customer messages');
    }
}

// Update customers list in sidebar
function updateCustomersList(customers) {
    const container = document.getElementById('customers-list');
    if (!container) return;
    
    if (customers.length === 0) {
        container.innerHTML = '<p class="text-muted">No active conversations</p>';
        return;
    }
    
    container.innerHTML = customers.map(customerId => `
        <div class="customer-chat-item" data-customer-id="${customerId}">
            <div class="customer-avatar">
                <i class="fas fa-user"></i>
            </div>
            <div class="customer-info">
                <h4>Customer #${customerId}</h4>
                <p class="text-muted">Has unread messages</p>
            </div>
            <div class="unread-indicator">
                <span class="badge badge-danger">New</span>
            </div>
        </div>
    `).join('');
}

// Select customer for chat
async function selectCustomer(customerId) {
    currentCustomerId = customerId;
    
    // Update UI to show selected customer
    document.querySelectorAll('.customer-chat-item').forEach(item => {
        item.classList.remove('active');
    });
    
    const selectedItem = document.querySelector(`[data-customer-id="${customerId}"]`);
    if (selectedItem) {
        selectedItem.classList.add('active');
    }
    
    // Load conversation
    await loadConversation(customerId);
    
    // Show chat interface
    document.getElementById('chat-interface').style.display = 'block';
    document.getElementById('no-chat-selected').style.display = 'none';
    
    // Mark conversation as read
    await markConversationAsRead(customerId);
}

// Load conversation messages
async function loadConversation(customerId) {
    try {
        AdminUtils.showLoading('chat-messages');
        
        const messages = await AdminUtils.makeApiCall(`/admin/chat/api/conversation/${customerId}`);
        if (messages) {
            displayMessages(messages);
            scrollToBottom();
        }
    } catch (error) {
        AdminUtils.showAlert('danger', 'Failed to load conversation');
    } finally {
        AdminUtils.hideLoading('chat-messages');
    }
}

// Display chat messages
function displayMessages(messages) {
    const container = document.getElementById('chat-messages');
    if (!container) return;
    
    container.innerHTML = messages.map(message => `
        <div class="message ${message.senderType.toLowerCase()}">
            <div class="message-content">
                <p>${escapeHtml(message.message)}</p>
                <small class="message-time">${AdminUtils.formatDateTime(message.timestamp)}</small>
            </div>
        </div>
    `).join('');
}

// Send message to customer
async function sendMessage(event) {
    event.preventDefault();
    
    if (!currentCustomerId) {
        AdminUtils.showAlert('warning', 'Please select a customer first');
        return;
    }
    
    const messageInput = document.getElementById('message-input');
    const message = messageInput.value.trim();
    
    if (!message) {
        AdminUtils.showAlert('warning', 'Please enter a message');
        return;
    }
    
    try {
        const response = await AdminUtils.makeApiCall('/admin/chat/api/send', 'POST', {
            customerId: currentCustomerId,
            message: message
        });
        
        if (response && response.success) {
            // Clear input
            messageInput.value = '';
            
            // Reload conversation to show new message
            await loadConversation(currentCustomerId);
            
            AdminUtils.showAlert('success', 'Message sent successfully');
        } else {
            AdminUtils.showAlert('danger', 'Failed to send message');
        }
    } catch (error) {
        AdminUtils.showAlert('danger', 'Error sending message');
    }
}

// Mark conversation as read
async function markConversationAsRead(customerId) {
    try {
        await AdminUtils.makeApiCall(`/admin/chat/api/mark-read/${customerId}`, 'POST');
        
        // Remove unread indicator
        const customerItem = document.querySelector(`[data-customer-id="${customerId}"]`);
        if (customerItem) {
            const badge = customerItem.querySelector('.badge');
            if (badge) {
                badge.remove();
            }
        }
    } catch (error) {
        console.error('Failed to mark conversation as read:', error);
    }
}

// Auto-resize textarea
function autoResizeTextarea(event) {
    const textarea = event.target;
    textarea.style.height = 'auto';
    textarea.style.height = textarea.scrollHeight + 'px';
}

// Scroll to bottom of chat
function scrollToBottom() {
    const container = document.getElementById('chat-messages');
    if (container) {
        container.scrollTop = container.scrollHeight;
    }
}

// Start auto-refresh for chat
function startChatRefresh() {
    chatRefreshInterval = setInterval(async function() {
        if (currentCustomerId) {
            await loadConversation(currentCustomerId);
        }
        await loadCustomersWithMessages();
    }, 3000); // Refresh every 3 seconds
}

// Update unread message count
function updateUnreadCount(count) {
    const badge = document.querySelector('.chat-notification-badge');
    if (badge) {
        if (count > 0) {
            badge.textContent = count > 99 ? '99+' : count;
            badge.style.display = 'block';
        } else {
            badge.style.display = 'none';
        }
    }
}

// Escape HTML to prevent XSS
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Cleanup on page unload
window.addEventListener('beforeunload', function() {
    if (chatRefreshInterval) {
        clearInterval(chatRefreshInterval);
    }
});
