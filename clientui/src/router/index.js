import { createWebHistory, createRouter } from "vue-router";

import About from "@/components/About.vue";
import Exports from "@/components/Exports.vue";
import Documentation from "@/components/Documentation.vue";
import ReportSelection from "@/components/ReportSelection.vue";
import ReadCodeEntry from "@/components/ReadCodeEntry.vue";
import SearchTermEntry from "@/components/SearchTermEntry.vue";
import ResolveBranchEntry from "@/components/ResolveBranchEntry.vue";
import Roles from "@/components/Roles.vue";
import Associations from "@/components/Associations.vue";

//Vue 3 routers were added to this new index.js file

const routes = [
    {
        path: "/",
        name: "Report Selection",
        component: ReportSelection,
        meta: {
            title: "test2"
        }
    },
    {
        path: "/report-exporter",
        name: "Report Selection",
        component: ReportSelection,
        meta: {
            title: "EVS Report Exporter - Report Selection"
        }
    },
    {
        path: '/readCodeEntry', component: ReadCodeEntry,
        meta: {
            title: "EVS Report Exporter - Read Code"
        },
        props: {
                msg: "selected tag",
                selectedTags:[]
               }
    },
    {
        path: '/searchTermEntry', component: SearchTermEntry },

    {
        path: '/resolveBranchEntry',
        component: ResolveBranchEntry,
        meta: {
            title: "EVS Report Exporter - Branch Resolve"
        }
    },
    {
        path: '/roles',
        component: Roles,
        meta: {
            title: "EVS Report Exporter - Roles"
        }
    },
    {
        path: '/associations',
        component: Associations,
        meta: {
            title: "EVS Report Exporter - Associations"
        }
    },
    { path: '/about', component: About },
    {
        path: '/documentation',
        component: Documentation,
        meta: {
            title: "EVS Report Exporter - Documentation"
        }
    },
    {
        path: "/exports",
        component: Exports,
        meta: {
            title: "EVS Report Exporter - Downloads"
        }
    },
    // if page is unknown, show main selection page.

    {
        path: '/:pathMatch(.*)*',
        redirect: "/ReportSelection",
    },
    ];

const router = createRouter({
    history: createWebHistory(),
    routes,
});


router.beforeEach((to, from, next ) => {
    document.title = `${to.meta.title}`;
    next();
});
export default router;