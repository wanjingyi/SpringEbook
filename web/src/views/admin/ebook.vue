<template>
    <a-layout>
        <a-layout-content :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }">
            <!-- <a-table :columns="columns" :data-source="ebooks" :pagination="pagination" :loading="loading"
                @change="handleTableChange">
                <template #bodyCell="{ column, text }">
                    <template v-if="column.dataIndex === 'name'">{{ text.first }} {{ text.last }}</template>
                </template>
            </a-table> -->
            <a-table :columns="columns" :data-source="ebooks" :pagination="pagination" :loading="loading"
                @change="handleTableChange">
                <template #headerCell="{ column }">
                    <template v-if="column.key === 'name'">
                        <span>
                            <smile-outlined />
                            Name
                        </span>
                    </template>
                </template>

                <template #bodyCell="{ column, record }">
                    <template v-if="column.key === 'name'">
                        <a>
                            {{ record.name }}
                        </a>
                    </template>
                    <template v-else-if="column.key === 'tags'">
                        <span>
                            <a-tag v-for="tag in record.tags" :key="tag"
                                :color="tag === 'loser' ? 'volcano' : tag.length > 5 ? 'geekblue' : 'green'">
                                {{ tag.toUpperCase() }}
                            </a-tag>
                        </span>
                    </template>
                    <template v-else-if="column.key === 'action'">
                        <span>
                            <a>Invite 一 {{ record.name }}</a>
                            <a-divider type="vertical" />
                            <a>Delete</a>
                            <a-divider type="vertical" />
                            <a class="ant-dropdown-link">
                                More actions
                                <down-outlined />
                            </a>
                        </span>
                    </template>
                </template>
            </a-table>
        </a-layout-content>
    </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, ref, computed } from 'vue';
import axios from 'axios';

export default defineComponent({
    name: 'adminEbook',
    setup() {

        const pagination = computed(() => ({
            total: 0,
            current: 1,
            pageSize: 5
        }));

        const loading = ref(false);

        const columns = [
            {
                name: 'name',
                dataIndex: 'name',
                key: 'name',
            },
            {
                title: 'category1_id',
                dataIndex: 'category1_id',
                key: 'category1_id',
            },
            {
                title: 'category2_id',
                dataIndex: 'category2_id',
                key: 'category2_id',
            },
            {
                title: 'description',
                key: 'description',
                dataIndex: 'description',
            },
            {
                title: 'cover',
                key: 'cover',
            },
            {
                title: 'doc_count',
                key: 'doc_count',
            },
            {
                title: 'view_count',
                key: 'view_count',
            },
            {
                title: 'vote_count',
                key: 'vote_count',
            }
        ];

        const ebooks = ref();

        /**
        * 数据查询
        */
        const handleQuery = (params: any) => {
            // loading.value = true;
            axios.get("/ebook/list", params).then((response) => {
                const data = response.data;
                ebooks.value = data.content;
                console.log(params);
                pagination.value.current = params.pageSize;
            });
        };

        const handleTableChange = (pagination: any) => {
            console.log(pagination)
            handleQuery({
                page: pagination.current,
                size: pagination.pageSize
            });
        };

        onMounted(() => {
            handleQuery({});
        })

        return {
            ebooks,
            columns,
            loading,
            pagination,
            handleTableChange
        }
    }
});
</script>