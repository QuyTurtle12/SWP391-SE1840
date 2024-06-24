class AuthRoleManager {
    static roles = {
        Manager: {
            allowedPages: ['/homepage', '/view-dashboard','/staff','/products/:id','profile','/productlist'],
        },
        Staff: {
            allowedPages: ['/homepage', '','/products/:id','profile','/productlist'],
        },
        Admin: {
            allowedPages: ['/homepage', '/adminPage', '/dashboard','/view-manager-list','/admin','/edit-manager/:id','/delete-manager/:id','profile','/productlist'],
        },
    };

    static getRoleID() {
        const userLoginBasicInformationDto = JSON.parse(localStorage.getItem('userLoginBasicInformationDto'));
        return userLoginBasicInformationDto ? userLoginBasicInformationDto.roleID : null;
    }

    static getAllowedPages() {
        const userRole = this.getRoleID();
        const allowedPages = userRole ? this.roles[userRole].allowedPages : [];
        console.log('Allowed Pages:', allowedPages);
        return allowedPages;
    }

    static isPageAllowed(page) {
        const allowedPages = this.getAllowedPages();
        const normalizedPage = `/${page.split('/').pop()}`; // Lấy phần cuối của URL và thêm '/' vào trước
        console.log('Normalized Page:', normalizedPage);
        console.log('Is Page Allowed:', allowedPages.includes(normalizedPage));
        return allowedPages.includes(normalizedPage);
    }
}

export default AuthRoleManager;