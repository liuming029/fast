const baseUrl = 'http://c8c03e8.r30.cpolar.top';

function request(options) {
  return new Promise((resolve, reject) => {
    const token = wx.getStorageSync('token');
    
    console.log('========== 发送请求 ==========');
    console.log('URL:', baseUrl + options.url);
    console.log('Method:', options.method || 'GET');
    console.log('Data:', options.data);
    
    wx.request({
      url: baseUrl + options.url,
      method: options.method || 'GET',
      data: options.data || {},
      header: {
        'Content-Type': 'application/json',
        'Authorization': token ? `Bearer ${token}` : ''
      },
      success: (res) => {
        console.log('========== 请求成功 ==========');
        console.log('Response:', res);
        
        if (res.statusCode === 200) {
          resolve(res.data);
        } else {
          reject(res.data);
        }
      },
      fail: (err) => {
        console.log('========== 请求失败 ==========');
        console.log('Error:', err);
        
        wx.showToast({
          title: '网络请求失败',
          icon: 'none',
          duration: 2000
        });
        reject(err);
      }
    });
  });
}

module.exports = request;