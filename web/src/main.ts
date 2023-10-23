import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
import * as Icons from '@ant-design/icons-vue';
import axios from 'axios';

axios.defaults.baseURL = process.env.VUE_APP_SERVER;

// 添加请求拦截器
axios.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    console.log("请求参数",config)
    return config;
}, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
});

// 添加响应拦截器
axios.interceptors.response.use(function (response) {
    // 对响应数据做点什么
    console.log("响应数据",response)
    return response;
}, function (error) {
    // 对响应错误做点什么
    console.log("响应错误",error)
    return Promise.reject(error);
});

const app = createApp(App)
const icons: any = Icons;
for (const i in icons) {
    app.component(i, icons[i]);
}
app.use(store).use(router).use(Antd).mount('#app')

// console.log('环境',process.env.NODE_ENV);
// console.log('服务端地址',process.env.VUE_APP_SERVER);