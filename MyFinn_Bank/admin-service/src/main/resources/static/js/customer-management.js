// Customer Management JavaScript
let selectedCustomers = [];

document.addEventListener('DOMContentLoaded', function() {
    loadCustomers();
    setupCustomerEventListeners();
    initializeCustomerFilters();
});

// Load all customers
async function loadCustomers() {
    try {
        AdminUtils.showLoading('customers-container');
        
        const customers = await AdminUtils.makeApiCall('/admin/customers/api/all');
        if (customers) {
            displayCustomers(customers);
        }
    } catch (error) {
        AdminUtils.showAlert('danger', 'Failed to load customers');
    } finally {
        AdminUtils.hideLoading('customers-container');
    }
}

// Display customers in table
function displayCustomers(customers) {
    const container = document.getElementById('customers-container');
    if (!container) return;
    
    if (customers.length === 0) {
        container.innerHTML = '<p class="text-center text-muted">No customers found</p>';
        return;
    }
    
    container.innerHTML = `
        <div class="customers-header">
            <div class="batch-actions">
                <button class="btn btn-warning" onclick="batchToggleStatus()" ${selectedCustomers.length === 0 ? 'disabled' : ''}>
                    Toggle Status (${selectedCustomers.length})
                </button>
                <button class="btn btn-info" onclick="exportCustomers()">
                    <i class="fas fa-download"></i> Export CSV
                </button>
            </div>
        </div>
        <div class="table-responsive">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>
                            <input type="checkbox" id="select-all-customers" onchange="toggleAllCustomers(event)">
                        </th>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Email</th>
                        <th>Phone</th>
                        <th>Status</th>
                        <th>Registered</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    ${customers.map(customer => createCustomerRow(customer)).join('')}
                </tbody>
            </table>
        </div>
    `;
}

// Create individual customer row
function createCustomerRow(customer) {
    return `
        <tr data-customer-id="${customer.customerId}">
            <td>
                <input type="checkbox" 
                       value="${customer.customerId}"
                       onchange="toggleCustomerSelection(${customer.customerId})">
            </td>
            <td>${customer.customerId}</td>
            <td>
                <div class="customer-info">
                    <strong>${customer.firstName} ${customer.lastName}</strong>
                    <br>
                    <small class="text-muted">${customer.address}</small>
                </div>
            </td>
            <td>${customer.email}</td>
            <td>${customer.phoneNumber}</td>
            <td>
                <span class="badge badge-${customer.isActive ? 'success' : 'danger'}">
                    ${customer.isActive ? 'Active' : 'Inactive'}
                </span>
            </td>
            <td>
                <small class="text-muted">
                    ${AdminUtils.formatDateTime(customer.createdDate)}
                </small>
            </td>
            <td>
                <div class="btn-group">
                    <button class="btn btn-sm btn-info" onclick="viewCustomer(${customer.customerId})">
                        <i class="fas fa-eye"></i>
                    </button>
                    <button class="btn btn-sm btn-${customer.isActive ? 'warning' : 'success'} toggle-status-btn"
                            data-customer-id="${customer.customerId}"
                            data-status="${customer.isActive}"
                            onclick="toggleCustomerStatus(${customer.customerId})">
                        <i class="fas fa-${customer.isActive ? 'ban' : 'check'}"></i>
                    </button>
                    <button class="btn btn-sm btn-primary" onclick="openCustomerChat(${customer.customerId})">
                        <i class="fas fa-comment"></i>
                    </button>
                </div>
            </td>
        </tr>
    `;
}

// Set up event listeners
function setupCustomerEventListeners() {
    // Search functionality
    const searchInput = document.getElementById('customer-search');
    if (searchInput) {
        searchInput.addEventListener('input', debounce(searchCustomers, 300));
    }
    
    // Filter by status
    const statusFilter = document.getElementById('status-filter');
    if (statusFilter) {
        statusFilter.addEventListener('change', function() {
            filterCustomersByStatus(this.value);
        });
    }
    
    // Sort functionality
    const sortSelect = document.getElementById('sort-customers');
    if (sortSelect) {
        sortSelect.addEventListener('change', function() {
            sortCustomers(this.value);
        });
    }
}

// Search customers
async function searchCustomers(event) {
    const searchTerm = event.target.value.trim();
    
    if (searchTerm.length === 0) {
        loadCustomers();
        return;
    }
    
    try {
        const customers = await AdminUtils.makeApiCall(`/admin/customers/api/search?keyword=${encodeURIComponent(searchTerm)}`);
        if (customers) {
            displayCustomers(customers);
        }
    } catch (error) {
        AdminUtils.showAlert('danger', 'Search failed');
    }
}

// Filter customers by status
function filterCustomersByStatus(status) {
    const rows = document.querySelectorAll('#customers-container tbody tr');
    
    rows.forEach(row => {
        const statusBadge = row.querySelector('.badge');
        const isActive = statusBadge.classList.contains('badge-success');
        
        let shouldShow = true;
        
        if (status === 'active' && !isActive) {
            shouldShow = false;
        } else if (status === 'inactive' && isActive) {
            shouldShow = false;
        }
        
        row.style.display = shouldShow ? '' : 'none';
    });
}

// Toggle customer selection
function toggleCustomerSelection(customerId) {
    const checkbox = document.querySelector(`input[value="${customerId}"]`);
    
    if (checkbox.checked) {
        if (!selectedCustomers.includes(customerId)) {
            selectedCustomers.push(customerId);
        }
    } else {
        selectedCustomers = selectedCustomers.filter(id => id !== customerId);
    }
    
    updateBatchActionButtons();
}

// Toggle all customers
function toggleAllCustomers(event) {
    const isChecked = event.target.checked;
    const checkboxes = document.querySelectorAll('#customers-container tbody input[type="checkbox"]');
    
    checkboxes.forEach(checkbox => {
        checkbox.checked = isChecked;
        const customerId = parseInt(checkbox.value);
        
        if (isChecked) {
            if (!selectedCustomers.includes(customerId)) {
                selectedCustomers.push(customerId);
            }
        } else {
            selectedCustomers = selectedCustomers.filter(id => id !== customerId);
        }
    });
    
    updateBatchActionButtons();
}

// Update batch action buttons
function updateBatchActionButtons() {
    const toggleBtn = document.querySelector('button[onclick="batchToggleStatus()"]');
    
    if (toggleBtn) {
        const hasSelection = selectedCustomers.length > 0;
        toggleBtn.disabled = !hasSelection;
        toggleBtn.textContent = `Toggle Status (${selectedCustomers.length})`;
    }
}

// View customer details
function viewCustomer(id) {
    window.location.href = `/admin/customers/${id}`;
}

// Toggle customer status
async function toggleCustomerStatus(customerId) {
    if (AdminUtils.confirmAction('toggle this customer\'s status')) {
        try {
            const response = await AdminUtils.makeApiCall(
                `/admin/customers/api/${customerId}/toggle-status`,
                'POST'
            );
            
            if (response && response.success) {
                AdminUtils.showAlert('success', response.message);
                await loadCustomers(); // Refresh list
            } else {
                AdminUtils.showAlert('danger', 'Failed to update customer status');
            }
        } catch (error) {
            AdminUtils.showAlert('danger', 'Error updating customer status');
        }
    }
}

// Batch toggle status
async function batchToggleStatus() {
    if (selectedCustomers.length === 0) return;
    
    if (AdminUtils.confirmAction(`toggle status for ${selectedCustomers.length} customers`)) {
        try {
            let successCount = 0;
            let errorCount = 0;
            
            for (const customerId of selectedCustomers) {
                try {
                    await AdminUtils.makeApiCall(
                        `/admin/customers/api/${customerId}/toggle-status`,
                        'POST'
                    );
                    successCount++;
                } catch (error) {
                    errorCount++;
                }
            }
            
            AdminUtils.showAlert('success', 
                `Updated ${successCount} customers successfully. ${errorCount} failed.`);
            
            selectedCustomers = [];
            await loadCustomers(); // Refresh list
            
        } catch (error) {
            AdminUtils.showAlert('danger', 'Batch operation failed');
        }
    }
}

// Open customer chat
function openCustomerChat(customerId) {
    window.location.href = `/admin/chat?customer=${customerId}`;
}

// Export customers to CSV
async function exportCustomers() {
    try {
        const customers = await AdminUtils.makeApiCall('/admin/customers/api/all');
        if (customers) {
            downloadCustomersCSV(customers);
        }
    } catch (error) {
        AdminUtils.showAlert('danger', 'Failed to export customers');
    }
}

// Download CSV
function downloadCustomersCSV(customers) {
    const headers = ['ID', 'First Name', 'Last Name', 'Email', 'Phone', 'Status', 'Registered Date'];
    const csvContent = [
        headers.join(','),
        ...customers.map(customer => [
            customer.customerId,
            customer.firstName,
            customer.lastName,
            customer.email,
            customer.phoneNumber,
            customer.isActive ? 'Active' : 'Inactive',
            customer.createdDate
        ].join(','))
    ].join('\n');
    
    const blob = new Blob([csvContent], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = `customers-${new Date().toISOString().split('T')[0]}.csv`;
    a.click();
    window.URL.revokeObjectURL(url);
}

// Sort customers
function sortCustomers(sortBy) {
    const tbody = document.querySelector('#customers-container tbody');
    if (!tbody) return;
    
    const rows = Array.from(tbody.querySelectorAll('tr'));
    
    rows.sort((a, b) => {
        let aValue, bValue;
        
        switch (sortBy) {
            case 'name':
                aValue = a.cells[2].textContent.trim();
                bValue = b.cells[2].textContent.trim();
                break;
            case 'email':
                aValue = a.cells[3].textContent.trim();
                bValue = b.cells[3].textContent.trim();
                break;
            case 'date':
                aValue = a.cells[6].textContent.trim();
                bValue = b.cells[6].textContent.trim();
                break;
            default:
                return 0;
        }
        
        return aValue.localeCompare(bValue);
    });
    
    // Re-append sorted rows
    rows.forEach(row => tbody.appendChild(row));
}

// Initialize filters
function initializeCustomerFilters() {
    console.log('Customer filters initialized');
}

// Debounce function for search
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}
