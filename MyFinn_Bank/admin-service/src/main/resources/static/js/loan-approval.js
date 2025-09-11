// Loan Approval JavaScript
let selectedLoans = [];

document.addEventListener('DOMContentLoaded', function() {
    loadPendingLoans();
    setupLoanEventListeners();
    initializeLoanFilters();
});

// Load pending loan applications
async function loadPendingLoans() {
    try {
        AdminUtils.showLoading('loans-container');
        
        const loans = await AdminUtils.makeApiCall('/admin/loans/api/pending');
        if (loans) {
            displayLoans(loans);
        }
    } catch (error) {
        AdminUtils.showAlert('danger', 'Failed to load loan applications');
    } finally {
        AdminUtils.hideLoading('loans-container');
    }
}

// Display loan applications
function displayLoans(loans) {
    const container = document.getElementById('loans-container');
    if (!container) return;
    
    if (loans.length === 0) {
        container.innerHTML = '<p class="text-center text-muted">No pending loan applications</p>';
        return;
    }
    
    container.innerHTML = `
        <div class="loans-header">
            <div class="batch-actions">
                <button class="btn btn-success" onclick="batchApprove()" ${selectedLoans.length === 0 ? 'disabled' : ''}>
                    Approve Selected (${selectedLoans.length})
                </button>
                <button class="btn btn-danger" onclick="batchReject()" ${selectedLoans.length === 0 ? 'disabled' : ''}>
                    Reject Selected (${selectedLoans.length})
                </button>
            </div>
        </div>
        <div class="loans-list">
            ${loans.map(loan => createLoanCard(loan)).join('')}
        </div>
    `;
}

// Create individual loan card
function createLoanCard(loan) {
    return `
        <div class="card loan-card" data-loan-id="${loan.loanId}">
            <div class="card-body">
                <div class="loan-header">
                    <div class="loan-checkbox">
                        <input type="checkbox" 
                               id="loan-${loan.loanId}" 
                               value="${loan.loanId}"
                               onchange="toggleLoanSelection(${loan.loanId})">
                    </div>
                    <div class="loan-info">
                        <h3>Application #${loan.loanId}</h3>
                        <p class="text-muted">${loan.customerName} (${loan.customerEmail})</p>
                    </div>
                    <div class="loan-amount">
                        <span class="amount">${AdminUtils.formatCurrency(loan.loanAmount)}</span>
                        <small class="loan-type">${loan.loanType.replace('_', ' ')}</small>
                    </div>
                </div>
                
                <div class="loan-details">
                    <div class="detail-row">
                        <span class="label">Interest Rate:</span>
                        <span class="value">${loan.interestRate}%</span>
                    </div>
                    <div class="detail-row">
                        <span class="label">Term:</span>
                        <span class="value">${loan.termMonths} months</span>
                    </div>
                    <div class="detail-row">
                        <span class="label">Monthly Income:</span>
                        <span class="value">${loan.monthlyIncome ? AdminUtils.formatCurrency(loan.monthlyIncome) : 'Not provided'}</span>
                    </div>
                    <div class="detail-row">
                        <span class="label">Purpose:</span>
                        <span class="value">${loan.purpose || 'Not provided'}</span>
                    </div>
                    <div class="detail-row">
                        <span class="label">Employment:</span>
                        <span class="value">${loan.employmentDetails || 'Not provided'}</span>
                    </div>
                    <div class="detail-row">
                        <span class="label">Applied:</span>
                        <span class="value">${AdminUtils.formatDateTime(loan.createdDate)}</span>
                    </div>
                </div>
                
                <div class="loan-actions">
                    <button class="btn btn-success approve-btn" 
                            onclick="approveLoan(${loan.loanId})">
                        <i class="fas fa-check"></i> Approve
                    </button>
                    <button class="btn btn-danger reject-btn" 
                            onclick="rejectLoan(${loan.loanId})">
                        <i class="fas fa-times"></i> Reject
                    </button>
                    <button class="btn btn-info" 
                            onclick="viewLoanDetails(${loan.loanId})">
                        <i class="fas fa-eye"></i> View Details
                    </button>
                </div>
            </div>
        </div>
    `;
}

// Set up event listeners
function setupLoanEventListeners() {
    // Status filter
    const statusFilter = document.getElementById('status-filter');
    if (statusFilter) {
        statusFilter.addEventListener('change', function() {
            loadLoansByStatus(this.value);
        });
    }
    
    // Search functionality
    const searchInput = document.getElementById('loan-search');
    if (searchInput) {
        searchInput.addEventListener('input', debounce(searchLoans, 300));
    }
    
    // Select all checkbox
    const selectAllCheckbox = document.getElementById('select-all-loans');
    if (selectAllCheckbox) {
        selectAllCheckbox.addEventListener('change', toggleAllLoans);
    }
}

// Toggle loan selection
function toggleLoanSelection(loanId) {
    const checkbox = document.getElementById(`loan-${loanId}`);
    
    if (checkbox.checked) {
        if (!selectedLoans.includes(loanId)) {
            selectedLoans.push(loanId);
        }
    } else {
        selectedLoans = selectedLoans.filter(id => id !== loanId);
    }
    
    updateBatchActionButtons();
}

// Toggle all loans selection
function toggleAllLoans(event) {
    const isChecked = event.target.checked;
    const checkboxes = document.querySelectorAll('.loan-card input[type="checkbox"]');
    
    checkboxes.forEach(checkbox => {
        checkbox.checked = isChecked;
        const loanId = parseInt(checkbox.value);
        
        if (isChecked) {
            if (!selectedLoans.includes(loanId)) {
                selectedLoans.push(loanId);
            }
        } else {
            selectedLoans = selectedLoans.filter(id => id !== loanId);
        }
    });
    
    updateBatchActionButtons();
}

// Update batch action buttons
function updateBatchActionButtons() {
    const approveBtn = document.querySelector('button[onclick="batchApprove()"]');
    const rejectBtn = document.querySelector('button[onclick="batchReject()"]');
    
    if (approveBtn && rejectBtn) {
        const hasSelection = selectedLoans.length > 0;
        approveBtn.disabled = !hasSelection;
        rejectBtn.disabled = !hasSelection;
        
        approveBtn.textContent = `Approve Selected (${selectedLoans.length})`;
        rejectBtn.textContent = `Reject Selected (${selectedLoans.length})`;
    }
}

// Approve single loan
async function approveLoan(loanId) {
    const remarks = prompt('Enter approval remarks (optional):') || 'Approved by admin';
    
    if (AdminUtils.confirmAction('approve this loan application')) {
        try {
            const response = await AdminUtils.makeApiCall(
                `/admin/loans/api/approve/${loanId}`,
                'POST',
                { remarks: remarks }
            );
            
            if (response && response.success) {
                AdminUtils.showAlert('success', 'Loan approved successfully');
                await loadPendingLoans(); // Refresh list
            } else {
                AdminUtils.showAlert('danger', 'Failed to approve loan');
            }
        } catch (error) {
            console.error('Approve loan error:', error);
            AdminUtils.showAlert('danger', 'Error approving loan: ' + error.message);
        }
    }
}

// Reject single loan
async function rejectLoan(loanId) {
    const remarks = prompt('Enter rejection reason:');
    
    if (remarks && AdminUtils.confirmAction('reject this loan application')) {
        try {
            const response = await AdminUtils.makeApiCall(
                `/admin/loans/api/reject/${loanId}`,
                'POST',
                { remarks: remarks }
            );
            
            if (response && response.success) {
                AdminUtils.showAlert('success', 'Loan rejected');
                await loadPendingLoans(); // Refresh list
            } else {
                AdminUtils.showAlert('danger', 'Failed to reject loan');
            }
        } catch (error) {
            console.error('Reject loan error:', error);
            AdminUtils.showAlert('danger', 'Error rejecting loan: ' + error.message);
        }
    }
}

// Batch approve selected loans
async function batchApprove() {
    if (selectedLoans.length === 0) return;
    
    const remarks = prompt('Enter approval remarks for all selected loans:') || 'Batch approved by admin';
    
    if (AdminUtils.confirmAction(`approve ${selectedLoans.length} loan applications`)) {
        try {
            const response = await AdminUtils.makeApiCall(
                '/admin/loans/api/batch-process',
                'POST',
                {
                    loanIds: selectedLoans,
                    action: 'approve',
                    remarks: remarks
                }
            );
            
            if (response && response.success) {
                AdminUtils.showAlert('success', response.message);
                selectedLoans = [];
                await loadPendingLoans(); // Refresh list
            } else {
                AdminUtils.showAlert('danger', 'Batch approval failed');
            }
        } catch (error) {
            AdminUtils.showAlert('danger', 'Error in batch approval');
        }
    }
}

// Batch reject selected loans
async function batchReject() {
    if (selectedLoans.length === 0) return;
    
    const remarks = prompt('Enter rejection reason for all selected loans:');
    
    if (remarks && AdminUtils.confirmAction(`reject ${selectedLoans.length} loan applications`)) {
        try {
            const response = await AdminUtils.makeApiCall(
                '/admin/loans/api/batch-process',
                'POST',
                {
                    loanIds: selectedLoans,
                    action: 'reject',
                    remarks: remarks
                }
            );
            
            if (response && response.success) {
                AdminUtils.showAlert('success', response.message);
                selectedLoans = [];
                await loadPendingLoans(); // Refresh list
            } else {
                AdminUtils.showAlert('danger', 'Batch rejection failed');
            }
        } catch (error) {
            AdminUtils.showAlert('danger', 'Error in batch rejection');
        }
    }
}

// Load loans by status
async function loadLoansByStatus(status) {
    try {
        AdminUtils.showLoading('loans-container');
        
        const url = status === 'ALL' ? '/admin/loans/api/all' : `/admin/loans/api/status/${status}`;
        const loans = await AdminUtils.makeApiCall(url);
        
        if (loans) {
            displayLoans(loans);
        }
    } catch (error) {
        AdminUtils.showAlert('danger', 'Failed to load loans');
    } finally {
        AdminUtils.hideLoading('loans-container');
    }
}

// Search loans functionality
function searchLoans(event) {
    const searchTerm = event.target.value.toLowerCase();
    const loanCards = document.querySelectorAll('.loan-card');
    
    loanCards.forEach(card => {
        const text = card.textContent.toLowerCase();
        const isVisible = text.includes(searchTerm);
        card.style.display = isVisible ? 'block' : 'none';
    });
}

// View loan details
function viewLoanDetails(loanId) {
    window.location.href = `/admin/loans/${loanId}`;
}

// Initialize loan filters
function initializeLoanFilters() {
    // Set default filter values if needed
    console.log('Loan filters initialized');
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
